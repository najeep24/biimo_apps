package com.example.finpro.Activity;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.finpro.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeServiceActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "description_updates_channel";

    private TextView tvMontirName, tvMontirPhone, tvMontirVehicle, tvMontirPlateNo;
    private TextView tvPickupAddress, vehicleTypeText, tvBookingDate, tvBookingTime;
    private TextView tvBrand, tvModel, tvPriceEstimation, tvYear, tvVariant, tvDescription;
    private TextView bookOrHome, dateText, timeText;
    private TextView montirLabel, alamatLabel, next;
    private LinearLayout platDanMotor;
    private String orderId;
    private boolean isSelfService;
    private View homeBtn, profileBtn, inboxBtn;
    private ImageView back, ilustrasi;
    private String previousDescription = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        createNotificationChannel();
        initializeViews();

        orderId = getIntent().getStringExtra("orderId");
        isSelfService = getIntent().getBooleanExtra("isSelfService", false);

        if (!isSelfService) {
            String montirName = getIntent().getStringExtra("montirName");
            String montirPhone = getIntent().getStringExtra("montirPhone");
            String montirVehicle = getIntent().getStringExtra("montirVehicle");
            String montirPlateNo = getIntent().getStringExtra("montirPlateNo");
            setMontirDetails(montirName, montirPhone, montirVehicle, montirPlateNo);
        } else {
            hideUnneededViews();
        }

        if (orderId != null) {
            fetchBookingDetails();
        } else {
            showError("Order details not found");
            finish();
        }
    }

    private void initializeViews() {
        ilustrasi = findViewById(R.id.ilustrasi);
        vehicleTypeText = findViewById(R.id.vehicleTypeText);
        tvMontirName = findViewById(R.id.tvMontirName);
        tvMontirPhone = findViewById(R.id.tvMontirPhone);
        next = findViewById(R.id.Next);
        tvMontirVehicle = findViewById(R.id.tvMontirVehicle);
        tvPickupAddress = findViewById(R.id.tvPickupAddress);
        tvMontirPlateNo = findViewById(R.id.tvMontirPlat);
        tvDescription = findViewById(R.id.tvDescription);
        bookOrHome = findViewById(R.id.bookOrHome);
        dateText = findViewById(R.id.dateText);
        timeText = findViewById(R.id.timeText);
        montirLabel = findViewById(R.id.montir);
        alamatLabel = findViewById(R.id.alamat);
        platDanMotor = findViewById(R.id.platDanMotor);
        profileBtn = findViewById(R.id.profileBtn);
        inboxBtn = findViewById(R.id.inboxBtn);
        homeBtn = findViewById(R.id.homeBtn);
        back = findViewById(R.id.back);

        next.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        back.setOnClickListener(v -> onBackPressed());
        homeBtn.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        profileBtn.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
        inboxBtn.setOnClickListener(v -> startActivity(new Intent(this, ChatActivity.class)));
    }

    private void hideUnneededViews() {
        montirLabel.setVisibility(View.GONE);
        tvMontirName.setVisibility(View.GONE);
        platDanMotor.setVisibility(View.GONE);
        tvMontirPhone.setVisibility(View.GONE);
        alamatLabel.setVisibility(View.GONE);
        tvPickupAddress.setVisibility(View.GONE);
    }

    private void setMontirDetails(String name, String phone, String vehicle, String plateNo) {
        tvMontirName.setText(name);
        tvMontirPhone.setText(phone);
        tvMontirVehicle.setText(vehicle);
        tvMontirPlateNo.setText(plateNo);
    }

    private void updateIlustrasiImage(String status) {
        int imageResource;
        switch (status.toLowerCase()) {
            case "montir bersiap":
            case "sedang siap menjemput":
                imageResource = R.drawable.driversiap2;
                break;
            case "montir sedang dalam perjalanan":
            case "sedang menjemput":
            case "driver sedang mengantar":
                imageResource = R.drawable.driverotw;
                break;
            case "montir sampai":
            case "driver sudah datang":
            case "driver sudah selesai mengantar":
                imageResource = R.drawable.driversudahdatang;
                break;
            case "completed":
                imageResource = R.drawable.orderselesai;
                break;
            case "booked":
                imageResource = R.drawable.orderditerima;
                break;
            case "on going":
                imageResource = R.drawable.driverongoing;
                break;
            default:
                imageResource = R.drawable.orderditerima;
                break;
        }
        ilustrasi.setImageResource(imageResource);
    }

    private void fetchBookingDetails() {
        DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference("bookings").child(orderId);

        bookingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) return;

                String status = dataSnapshot.child("status").getValue(String.class);
                if (status != null) updateIlustrasiImage(status);

                String description = dataSnapshot.child("description").getValue(String.class);
                if (description != null && !description.equals(previousDescription)) {
                    if (previousDescription != null) {
                        showSystemNotification("Booking Update", description);
                    }
                    previousDescription = description;
                }

                String brand = dataSnapshot.child("brand").getValue(String.class);
                String model = dataSnapshot.child("model").getValue(String.class);
                String bookingDate = dataSnapshot.child("bookingDate").getValue(String.class);
                String bookingTime = dataSnapshot.child("bookingTime").getValue(String.class);
                String pickupAddress = dataSnapshot.child("pickupAddress").getValue(String.class);
                String year = dataSnapshot.child("year").getValue(String.class);
                String variant = dataSnapshot.child("variant").getValue(String.class);
                String serviceCategory = dataSnapshot.child("serviceCategory").getValue(String.class);

                if (tvBrand != null && brand != null) tvBrand.setText(brand.toUpperCase());
                if (tvModel != null && model != null) tvModel.setText(model.toUpperCase());
                if (!isSelfService && pickupAddress != null) tvPickupAddress.setText(pickupAddress);
                if (tvYear != null && year != null) tvYear.setText(year);
                if (tvVariant != null && variant != null) tvVariant.setText(variant);
                if (vehicleTypeText != null) vehicleTypeText.setText("Motor");
                if (tvDescription != null && description != null) tvDescription.setText(description);

                if (bookOrHome != null) {
                    if ("homeServices".equals(serviceCategory)) {
                        bookOrHome.setText("Home Service");
                    } else {
                        bookOrHome.setText(isSelfService ? "Self Service" : "Onsite Service");
                    }
                }

                if (dateText != null && bookingDate != null) {
                    try {
                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault());
                        Date date = inputFormat.parse(bookingDate);
                        dateText.setText(date != null ? outputFormat.format(date) : bookingDate);
                    } catch (ParseException e) {
                        dateText.setText(bookingDate);
                    }
                }

                if (timeText != null && bookingTime != null) timeText.setText(bookingTime);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                showError("Failed to load booking: " + error.getMessage());
            }
        });
    }

    private void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void showSystemNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.biimologo)  // make sure this icon exists in res/drawable
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        manager.notify(1, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Description Update";
            String description = "Channel for booking description updates";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) manager.createNotificationChannel(channel);
        }
    }
}

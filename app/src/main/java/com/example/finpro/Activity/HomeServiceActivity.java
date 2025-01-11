package com.example.finpro.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.finpro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

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
            // Hide montir-related views for self-service
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

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeServiceActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // This will go back to the previous activity
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeServiceActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Set onClickListener for profileBtn
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeServiceActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        // Set onClickListener for inboxBtn
        inboxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeServiceActivity.this, ChatActivity.class); // Navigate to ChatActivity
                startActivity(intent);
            }
        });
    }

    private void hideUnneededViews() {
        // Hide montir label and name
        if (montirLabel != null) montirLabel.setVisibility(View.GONE);
        if (tvMontirName != null) tvMontirName.setVisibility(View.GONE);

        // Hide vehicle and plate info container
        if (platDanMotor != null) platDanMotor.setVisibility(View.GONE);
        if (tvMontirPhone != null) tvMontirPhone.setVisibility(View.GONE);

        // Hide address label and value
        if (alamatLabel != null) alamatLabel.setVisibility(View.GONE);
        if (tvPickupAddress != null) tvPickupAddress.setVisibility(View.GONE);
    }

    private void setMontirDetails(String name, String phone, String vehicle, String plateNo) {
        if (tvMontirName != null) tvMontirName.setText(name);
        if (tvMontirPhone != null) tvMontirPhone.setText(phone);
        if (tvMontirVehicle != null) tvMontirVehicle.setText(vehicle);
        if (tvMontirPlateNo != null) tvMontirPlateNo.setText(plateNo);
    }

    private void updateIlustrasiImage(String status) {
        if (ilustrasi != null) {
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
                    imageResource = R.drawable.orderditerima; // Default image
                    break;
            }
            ilustrasi.setImageResource(imageResource);
        }
    }

    private void fetchBookingDetails() {
        DatabaseReference bookingRef = FirebaseDatabase.getInstance()
                .getReference("bookings")
                .child(orderId);

        bookingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String status = dataSnapshot.child("status").getValue(String.class);
                    if (status != null) {
                        updateIlustrasiImage(status);
                    }

                    String brand = dataSnapshot.child("brand").getValue(String.class);
                    String model = dataSnapshot.child("model").getValue(String.class);
                    String bookingDate = dataSnapshot.child("bookingDate").getValue(String.class);
                    String bookingTime = dataSnapshot.child("bookingTime").getValue(String.class);
                    String pickupAddress = dataSnapshot.child("pickupAddress").getValue(String.class);
                    String year = dataSnapshot.child("year").getValue(String.class);
                    String variant = dataSnapshot.child("variant").getValue(String.class);
                    String description = dataSnapshot.child("description").getValue(String.class);
                    String serviceCategory = dataSnapshot.child("serviceCategory").getValue(String.class);

                    // Set data to views
                    if (brand != null && tvBrand != null) tvBrand.setText(brand.toUpperCase());
                    if (model != null && tvModel != null) tvModel.setText(model.toUpperCase());
                    if (!isSelfService && pickupAddress != null && tvPickupAddress != null) {
                        tvPickupAddress.setText(pickupAddress);
                    }
                    if (year != null && tvYear != null) tvYear.setText(year);
                    if (variant != null && tvVariant != null) tvVariant.setText(variant);
                    if (vehicleTypeText != null) vehicleTypeText.setText("Motor");
                    if (description != null && tvDescription != null) tvDescription.setText(description);

                    // Set service type text
                    if (bookOrHome != null) {
                        if ("homeServices".equals(serviceCategory)) {
                            bookOrHome.setText("Home Service");
                        } else if ("bookServices".equals(serviceCategory)) {
                            bookOrHome.setText(isSelfService ? "Self Service" : "Onsite Service");
                        }
                    }

                    // Set date and time
                    if (dateText != null && bookingDate != null) {
                        try {
                            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault());
                            Date date = inputFormat.parse(bookingDate);
                            if (date != null) {
                                dateText.setText(outputFormat.format(date));
                            } else {
                                dateText.setText(bookingDate);
                            }
                        } catch (ParseException e) {
                            dateText.setText(bookingDate);
                        }
                    }
                    if (timeText != null && bookingTime != null) timeText.setText(bookingTime);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showError("Failed to load booking details: " + databaseError.getMessage());
            }
        });
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}

package com.example.finpro.Activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.finpro.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeServiceActivity extends AppCompatActivity {

    private TextView tvBookingDate, tvBookingTime, tvBrand, tvModel;
    private TextView tvMontirName, tvMontirPhone, tvMontirVehicle, tvMontirPlateNo;
    private TextView tvPickupAddress, tvPriceEstimation, vehicleTypeText;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        // Initialize views
        initializeViews();

        // Get data from intent
        orderId = getIntent().getStringExtra("orderId");
        String montirName = getIntent().getStringExtra("montirName");
        String montirPhone = getIntent().getStringExtra("montirPhone");
        String montirVehicle = getIntent().getStringExtra("montirVehicle");
        String montirPlateNo = getIntent().getStringExtra("montirPlateNo");

        // Set montir details received from intent
        setMontirDetails(montirName, montirPhone, montirVehicle, montirPlateNo);

        // Fetch additional booking details from Firebase
        if (orderId != null) {
            fetchBookingDetails();
        } else {
            showError("Order details not found");
            finish();
        }
    }

    private void initializeViews() {
        vehicleTypeText = findViewById(R.id.vehicleTypeText);
        tvBrand = findViewById(R.id.tvBrand);
        tvModel = findViewById(R.id.tvModel);
        tvBookingDate = findViewById(R.id.tvBookingDate);
        tvBookingTime = findViewById(R.id.tvBookingTime);
        tvMontirName = findViewById(R.id.tvMontirName);
        tvMontirPhone = findViewById(R.id.tvMontirPhone);
        tvMontirVehicle = findViewById(R.id.tvMontirVehicle);
        tvPickupAddress = findViewById(R.id.tvPickupAddress);
        tvPriceEstimation = findViewById(R.id.tvPriceEstimation);
    }

    private void setMontirDetails(String name, String phone, String vehicle, String plateNo) {
        if (tvMontirName != null) tvMontirName.setText(name);
        if (tvMontirPhone != null) tvMontirPhone.setText(phone);
        if (tvMontirVehicle != null) tvMontirVehicle.setText(vehicle);
        if (tvMontirPlateNo != null) tvMontirPlateNo.setText(plateNo);
    }

    private void fetchBookingDetails() {
        DatabaseReference bookingRef = FirebaseDatabase.getInstance()
                .getReference("bookings")
                .child(orderId);

        bookingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Get and set vehicle details
                    String brand = dataSnapshot.child("brand").getValue(String.class);
                    String model = dataSnapshot.child("model").getValue(String.class);
                    String bookingDate = dataSnapshot.child("bookingDate").getValue(String.class);
                    String bookingTime = dataSnapshot.child("bookingTime").getValue(String.class);
                    String pickupAddress = dataSnapshot.child("pickupAddress").getValue(String.class);
                    String priceEstimation = dataSnapshot.child("priceEstimation").getValue(String.class);

                    // Set values to TextViews
                    if (brand != null) tvBrand.setText(brand.toUpperCase());
                    if (model != null) tvModel.setText(model.toUpperCase());
                    if (bookingDate != null) tvBookingDate.setText(bookingDate);
                    if (bookingTime != null) tvBookingTime.setText(bookingTime);
                    if (pickupAddress != null) tvPickupAddress.setText(pickupAddress);

                    // Set vehicle type (assuming it's always "Motor" based on the layout)
                    vehicleTypeText.setText("Motor");
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
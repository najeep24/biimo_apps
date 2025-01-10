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

    private TextView tvMontirName, tvMontirPhone, tvMontirVehicle, tvMontirPlateNo;
    private TextView tvPickupAddress, vehicleTypeText, tvBookingDate, tvBookingTime, tvBrand, tvModel, tvPriceEstimation, tvYear, tvVariant, tvDescription;  // Add tvDescription
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
        // Ensure these are initialized before using
        vehicleTypeText = findViewById(R.id.vehicleTypeText);
        tvMontirName = findViewById(R.id.tvMontirName);
        tvMontirPhone = findViewById(R.id.tvMontirPhone);
        tvMontirVehicle = findViewById(R.id.tvMontirVehicle);
        tvPickupAddress = findViewById(R.id.tvPickupAddress);
        tvDescription = findViewById(R.id.tvDescription);  // Initialize tvDescription
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
                    // Get additional details
                    String brand = dataSnapshot.child("brand").getValue(String.class);
                    String model = dataSnapshot.child("model").getValue(String.class);
                    String bookingDate = dataSnapshot.child("bookingDate").getValue(String.class);
                    String bookingTime = dataSnapshot.child("bookingTime").getValue(String.class);
                    String pickupAddress = dataSnapshot.child("pickupAddress").getValue(String.class);
                    String priceEstimation = dataSnapshot.child("priceEstimation").getValue(String.class);
                    String year = dataSnapshot.child("year").getValue(String.class);
                    String variant = dataSnapshot.child("variant").getValue(String.class);
                    String description = dataSnapshot.child("description").getValue(String.class);  // Fetch description

                    // Set data to views
                    if (brand != null && tvBrand != null) tvBrand.setText(brand.toUpperCase());
                    if (model != null && tvModel != null) tvModel.setText(model.toUpperCase());
                    if (bookingDate != null && tvBookingDate != null) tvBookingDate.setText(bookingDate);
                    if (bookingTime != null && tvBookingTime != null) tvBookingTime.setText(bookingTime);
                    if (pickupAddress != null && tvPickupAddress != null) tvPickupAddress.setText(pickupAddress);
                    if (year != null && tvYear != null) tvYear.setText(year);
                    if (variant != null && tvVariant != null) tvVariant.setText(variant);
                    if (vehicleTypeText != null) {
                        vehicleTypeText.setText("Motor");
                    }

                    // Set description to the new TextView
                    if (description != null && tvDescription != null) tvDescription.setText(description);
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

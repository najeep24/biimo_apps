package com.example.finpro.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.finpro.R;
import com.example.finpro.Viewmodel.SummaryOrderViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class SummaryOrder extends AppCompatActivity {

    private static final String TAG = "SummaryOrder";
    private DatabaseReference ordersReference;
    private DatabaseReference usersReference;
    private String currentOrderId;

    private TextView addressLabel;
    private TextView addressValue;
    private TextView bookOrHome;
    private TextView vehicleTypeText;
    private TextView brandText;
    private TextView modelText;
    private TextView variantText;
    private TextView yearText;
    private TextView serviceTypeText;
    private TextView dateText;
    private TextView timeText;
    private TextView priceEstimationText;
    private ImageView vehicleImage;
    private TextView next;

    private SummaryOrderViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_order);

        initializeViews();
        setupFirebase();
        setupViewModel();
        setupClickListeners();
        observeViewModel();
        handleIntentData();
    }

    private void initializeViews() {
        addressLabel = findViewById(R.id.textView14);
        addressValue = findViewById(R.id.textView15);
        bookOrHome = findViewById(R.id.bookOrHome);
        vehicleTypeText = findViewById(R.id.vehicleTypeText);
        brandText = findViewById(R.id.brandText);
        modelText = findViewById(R.id.modelText);
        variantText = findViewById(R.id.variantText);
        yearText = findViewById(R.id.yearText);
        serviceTypeText = findViewById(R.id.serviceTypeText);
        dateText = findViewById(R.id.dateText);
        timeText = findViewById(R.id.timeText);
        priceEstimationText = findViewById(R.id.priceEstimationText);
        vehicleImage = findViewById(R.id.vehicleImage);
        next = findViewById(R.id.Next);
    }

    private void setupFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ordersReference = database.getReference("bookings");
        usersReference = database.getReference("users");
        currentOrderId = ordersReference.push().getKey();
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(SummaryOrderViewModel.class);
    }

    private void setupClickListeners() {
        next.setOnClickListener(view -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String userId = user.getUid();
                String serviceCategory = viewModel.getServiceCategory().getValue();
                fetchUserDataAndCreateBooking(userId, serviceCategory);
            } else {
                Log.e(TAG, "User is not authenticated");
                showError("Please log in to continue");
            }
        });
    }

    private void handleIntentData() {
        Intent intent = getIntent();
        String pickupAddress = intent.getStringExtra("pickupAddress");
        String serviceCategory = intent.getStringExtra("serviceCategory");
        boolean isSelfService = intent.getBooleanExtra("isSelfService", false);

        handleAddressVisibility(serviceCategory, isSelfService, pickupAddress);
        setViewModelData(intent.getExtras(), serviceCategory);

        // Set the serviceCategory to the bookOrHome TextView with custom display
        if (serviceCategory != null) {
            if ("homeServices".equals(serviceCategory)) {
                bookOrHome.setText("Home Service");
            } else if ("bookServices".equals(serviceCategory)) {
                bookOrHome.setText("Booking Service");
            } else {
                bookOrHome.setText("Unknown Service");
            }
        }
    }


    private void handleAddressVisibility(String serviceCategory, boolean isSelfService, String pickupAddress) {
        if ("homeServices".equals(serviceCategory)) {
            showAddress(true);
            setAddress(pickupAddress, "Address not provided");
        } else if ("bookServices".equals(serviceCategory) && isSelfService) {
            showAddress(false);
        } else {
            showAddress(true);
            setAddress(pickupAddress, "No address available");
        }
    }

    private void showAddress(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        addressLabel.setVisibility(visibility);
        addressValue.setVisibility(visibility);
    }

    private void setAddress(String address, String defaultValue) {
        viewModel.setAddress(address != null && !address.isEmpty() ? address : defaultValue);
    }

    private void setViewModelData(Bundle extras, String serviceCategory) {
        if (extras != null) {
            viewModel.setVehicleType(extras.getString("vehicleType", ""));
            viewModel.setBrand(extras.getString("brand", ""));
            viewModel.setModel(extras.getString("model", ""));
            viewModel.setVariant(extras.getString("variant", ""));
            viewModel.setYear(extras.getString("year", ""));
            viewModel.setServiceType(extras.getString("serviceType", ""));

            String bookingDate = extras.getString("bookingDate", "");
            if (bookingDate != null && !bookingDate.isEmpty()) {
                try {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault());
                    Date date = inputFormat.parse(bookingDate);
                    viewModel.setBookingDate(date != null ? outputFormat.format(date) : "Invalid date");
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing date: " + e.getMessage());
                    viewModel.setBookingDate("Invalid date");
                }
            } else {
                viewModel.setBookingDate("No date provided");
            }

            viewModel.setBookingTime(extras.getString("bookingTime", ""));
            viewModel.setPriceEstimation(extras.getString("priceEstimation", ""));
            viewModel.setServiceCategory(serviceCategory);
        }
    }

    private void fetchUserDataAndCreateBooking(String userId, String serviceCategory) {
        showLoading(true);
        usersReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String userName = snapshot.child("name").getValue(String.class);
                    String userEmail = snapshot.child("email").getValue(String.class);
                    String userPhone = snapshot.child("phone").getValue(String.class);

                    // Ensure we have user data, falling back to default values if not present
                    userName = userName != null ? userName : "Anonymous User";
                    userEmail = userEmail != null ? userEmail : "No email provided";
                    userPhone = userPhone != null ? userPhone : "No phone number provided";

                    // Create booking data
                    Map<String, Object> bookingData = createBookingData(userId, userName, userEmail, userPhone);

                    // Fetch montir and save booking
                    fetchMontirAndSaveBooking(bookingData, serviceCategory);
                } else {
                    showLoading(false);
                    Log.e(TAG, "User data not found in database");
                    showError("Failed to fetch user details. Please try again.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showLoading(false);
                Log.e(TAG, "Error fetching user data: " + error.getMessage());
                showError("Network error while fetching user details. Please try again.");
            }
        });
    }


    private void fetchMontirAndSaveBooking(Map<String, Object> bookingData, String serviceCategory) {
        DatabaseReference montirRef = FirebaseDatabase.getInstance().getReference("Montir");
        DatabaseReference bookingsRef = FirebaseDatabase.getInstance().getReference("bookings");
        String selectedDate = bookingData.get("bookingDate").toString();

        // First, get all existing bookings for the selected date
        bookingsRef.orderByChild("bookingDate").equalTo(selectedDate)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot bookingsSnapshot) {
                        // Create a set of booked montir names for the selected date
                        Set<String> bookedMontirs = new HashSet<>();
                        for (DataSnapshot booking : bookingsSnapshot.getChildren()) {
                            String montirName = booking.child("montirName").getValue(String.class);
                            if (montirName != null) {
                                bookedMontirs.add(montirName);
                            }
                        }

                        // Now fetch available montirs
                        montirRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot montirSnapshot) {
                                ArrayList<DataSnapshot> availableMontirs = new ArrayList<>();

                                // Filter out already booked montirs
                                for (DataSnapshot montir : montirSnapshot.getChildren()) {
                                    String montirName = montir.child("Nama").getValue(String.class);
                                    if (montirName != null && !bookedMontirs.contains(montirName)) {
                                        availableMontirs.add(montir);
                                    }
                                }

                                if (!availableMontirs.isEmpty()) {
                                    // Randomly select from available montirs
                                    int randomIndex = new Random().nextInt(availableMontirs.size());
                                    DataSnapshot selectedMontir = availableMontirs.get(randomIndex);

                                    // Add montir details to booking data
                                    bookingData.put("montirName", selectedMontir.child("Nama").getValue(String.class));
                                    bookingData.put("montirPhone", selectedMontir.child("No telpon").getValue(String.class));
                                    bookingData.put("montirVehicle", selectedMontir.child("Merk Motor").getValue(String.class));
                                    bookingData.put("montirPlateNo", selectedMontir.child("Plat No").getValue(String.class));

                                    saveBookingToFirebase(bookingData, serviceCategory);
                                } else {
                                    showLoading(false);
                                    showError("No available montirs for the selected date. Please choose a different date.");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                showLoading(false);
                                Log.e(TAG, "Error fetching montir data: " + databaseError.getMessage());
                                showError("Error checking montir availability. Please try again.");
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        showLoading(false);
                        Log.e(TAG, "Error fetching bookings: " + databaseError.getMessage());
                        showError("Error checking montir schedules. Please try again.");
                    }
                });
    }

    // Helper method to check if two booking times overlap
    private boolean isTimeOverlap(String existingTime, String newTime) {
        try {
            // Parse times (assuming format like "09:00" or "14:30")
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date existingBookingTime = timeFormat.parse(existingTime);
            Date newBookingTime = timeFormat.parse(newTime);

            if (existingBookingTime == null || newBookingTime == null) {
                return false;
            }

            // Calculate time difference in milliseconds
            long timeDifference = Math.abs(existingBookingTime.getTime() - newBookingTime.getTime());
            // Consider bookings within 2 hours of each other as overlapping
            long twoHoursInMillis = 2 * 60 * 60 * 1000;

            return timeDifference < twoHoursInMillis;
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing time: " + e.getMessage());
            return false;
        }
    }

    // Modified createBookingData to ensure date format consistency
    private Map<String, Object> createBookingData(String userId, String userName, String userEmail, String userPhone) {
        Map<String, Object> bookingData = new HashMap<>();

        // Get the booking date from the TextView
        String rawDate = dateText.getText().toString();

        // Convert the formatted date back to a standard format for storage
        try {
            SimpleDateFormat displayFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault());
            SimpleDateFormat storageFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = displayFormat.parse(rawDate);
            String standardizedDate = date != null ? storageFormat.format(date) : rawDate;

            bookingData.put("bookingDate", standardizedDate);
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing date: " + e.getMessage());
            bookingData.put("bookingDate", rawDate); // Use raw date if parsing fails
        }

        // Add the rest of the booking data
        bookingData.put("userId", userId);
        bookingData.put("userName", userName != null ? userName : "Anonymous User");
        bookingData.put("userEmail", userEmail);
        bookingData.put("userPhone", userPhone);
        bookingData.put("vehicleType", vehicleTypeText.getText().toString());
        bookingData.put("brand", brandText.getText().toString());
        bookingData.put("model", modelText.getText().toString());
        bookingData.put("variant", variantText.getText().toString());
        bookingData.put("year", yearText.getText().toString());
        bookingData.put("serviceType", serviceTypeText.getText().toString());
        bookingData.put("bookingTime", timeText.getText().toString());
        bookingData.put("priceEstimation", priceEstimationText.getText().toString());
        bookingData.put("status", "pending");
        bookingData.put("createdAt", ServerValue.TIMESTAMP);

        String serviceCategory = viewModel.getServiceCategory().getValue();
        bookingData.put("serviceCategory", serviceCategory != null ? serviceCategory : "unknown");

        String pickupAddress = addressValue.getText().toString();
        if (!pickupAddress.isEmpty() && !"No address available".equals(pickupAddress)) {
            bookingData.put("pickupAddress", pickupAddress);
        }

        return bookingData;
    }

    private void saveBookingToFirebase(Map<String, Object> bookingData, String serviceCategory) {
        DatabaseReference currentOrderRef = ordersReference.child(currentOrderId);
        currentOrderRef.setValue(bookingData)
                .addOnCompleteListener(task -> {
                    showLoading(false);
                    if (task.isSuccessful()) {
                        Intent intent;
                        if ("homeServices".equalsIgnoreCase(serviceCategory) || "pickup".equalsIgnoreCase(serviceCategory)) {
                            intent = new Intent(SummaryOrder.this, HomeServiceActivity.class);
                            // Pass montir details to HomeServiceActivity
                            intent.putExtra("montirName", bookingData.get("montirName").toString());
                            intent.putExtra("montirPhone", bookingData.get("montirPhone").toString());
                            intent.putExtra("montirVehicle", bookingData.get("montirVehicle").toString());
                            intent.putExtra("montirPlateNo", bookingData.get("montirPlateNo").toString());
                            intent.putExtra("orderId", currentOrderId);
                        } else {
                            intent = new Intent(SummaryOrder.this, OrderSucced.class);
                        }
                        startActivity(intent);
                        finish();
                    } else {
                        showError("Failed to save booking. Please try again.");
                    }
                })
                .addOnFailureListener(e -> {
                    showLoading(false);
                    Log.e(TAG, "Error saving booking data: " + e.getMessage());
                    showError("Network error. Please try again.");
                });
    }

    private void showLoading(boolean isLoading) {
        if (next != null) {
            next.setEnabled(!isLoading);
            next.setText(isLoading ? "Processing..." : "Proceed");
        }
    }

    private void showError(String message) {
        Toast.makeText(SummaryOrder.this, message, Toast.LENGTH_LONG).show();
    }

    private void observeViewModel() {
        viewModel.getVehicleType().observe(this, value -> vehicleTypeText.setText(value));
        viewModel.getBrand().observe(this, value -> brandText.setText(value));
        viewModel.getModel().observe(this, value -> modelText.setText(value));
        viewModel.getVariant().observe(this, value -> variantText.setText(value));
        viewModel.getYear().observe(this, value -> yearText.setText(value));
        viewModel.getServiceType().observe(this, value -> serviceTypeText.setText(value));
        viewModel.getBookingDate().observe(this, value -> dateText.setText(value));
        viewModel.getBookingTime().observe(this, value -> timeText.setText(value));
        viewModel.getPriceEstimation().observe(this, value -> priceEstimationText.setText(value));
        viewModel.getAddress().observe(this, value -> addressValue.setText(value));
    }
}


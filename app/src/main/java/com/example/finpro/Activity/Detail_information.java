package com.example.finpro.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finpro.Adaptor.SpinnerAdaptor;
import com.example.finpro.R;
import com.example.finpro.Viewmodel.DetailInformationViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.lifecycle.ViewModelProvider;


public class Detail_information extends AppCompatActivity {
    private TextView next;
    private Spinner spinnerBrands, spinnerModels, spinnerVariants, spinnerYears;
    private RadioGroup radioGroup;
    private RadioButton carButton, motorcycleButton;
    private DetailInformationViewModel viewModel;
    private String serviceCategory;
    private View homeBtn, profileBtn, inboxBtn, activityBtn;
    private ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_information);


        viewModel = new ViewModelProvider(this).get(DetailInformationViewModel.class);
        initializeViews();
        setupListeners();
        observeViewModel();

        // Load initial data
        viewModel.loadBrands();

        serviceCategory = getIntent().getStringExtra("serviceCategory");
        if ("homeServices".equals(serviceCategory)) {
            carButton.setEnabled(false);
            carButton.setBackgroundResource(R.drawable.kendaraan_disable_background);
            radioGroup.check(R.id.radioBtn_motor);
        }
    }

    private void initializeViews() {
        next = findViewById(R.id.Next);
        carButton = findViewById(R.id.radioBtn_mobil);
        motorcycleButton = findViewById(R.id.radioBtn_motor);
        radioGroup = findViewById(R.id.RadioGroupSelect);
        spinnerBrands = findViewById(R.id.spinner_brands);
        spinnerModels = findViewById(R.id.spinner_models);
        spinnerVariants = findViewById(R.id.spinner_variants);
        spinnerYears = findViewById(R.id.spinner_years);
        profileBtn = findViewById(R.id.profileBtn);
        inboxBtn = findViewById(R.id.inboxBtn);
        activityBtn = findViewById(R.id.activityBtn);
        homeBtn = findViewById(R.id.homeBtn);
        back = findViewById(R.id.back);

        radioGroup.check(R.id.radioBtn_motor);
    }

    private void setupListeners() {
        next.setOnClickListener(v -> {
            Intent intent = new Intent(Detail_information.this, ServiceType.class);
            String vehicleType = carButton.isChecked() ? "car" : "motorcycle";
            intent.putExtra("vehicleType", vehicleType);
            intent.putExtra("brand", spinnerBrands.getSelectedItem().toString());
            intent.putExtra("model", spinnerModels.getSelectedItem().toString());
            intent.putExtra("variant", spinnerVariants.getSelectedItem().toString());
            intent.putExtra("year", spinnerYears.getSelectedItem().toString());
            intent.putExtra("serviceCategory", serviceCategory);
            startActivity(intent);
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
                Intent intent = new Intent(Detail_information.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Set onClickListener for profileBtn
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Detail_information.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        // Set onClickListener for inboxBtn
        inboxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Detail_information.this, ChatActivity.class); // Navigate to ChatActivity
                startActivity(intent);
            }
        });

        activityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser == null) {
                    Toast.makeText(Detail_information.this, "Please login first", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseReference bookingsRef = FirebaseDatabase.getInstance().getReference("bookings");
                bookingsRef.orderByChild("userId")
                        .equalTo(currentUser.getUid())
                        .limitToLast(1)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        String orderId = snapshot.getKey();
                                        String serviceCategory = snapshot.child("serviceCategory").getValue(String.class);
                                        boolean isSelfService = Boolean.TRUE.equals(
                                                snapshot.child("isSelfService").getValue(Boolean.class));

                                        Intent intent = new Intent(Detail_information.this, HomeServiceActivity.class);
                                        intent.putExtra("orderId", orderId);
                                        intent.putExtra("isSelfService", isSelfService);

                                        // Add montir details if not self-service
                                        if (!isSelfService) {
                                            intent.putExtra("montirName",
                                                    snapshot.child("montirName").getValue(String.class));
                                            intent.putExtra("montirPhone",
                                                    snapshot.child("montirPhone").getValue(String.class));
                                            intent.putExtra("montirVehicle",
                                                    snapshot.child("montirVehicle").getValue(String.class));
                                            intent.putExtra("montirPlateNo",
                                                    snapshot.child("montirPlateNo").getValue(String.class));
                                        }

                                        startActivity(intent);
                                        return;
                                    }
                                } else {
                                    Toast.makeText(Detail_information.this,
                                            "No active orders found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(Detail_information.this,
                                        "Error fetching orders: " + databaseError.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });


        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String vehicleType = (checkedId == R.id.radioBtn_motor) ? "motorcycle" : "cars";
            viewModel.setVehicleType(vehicleType);
        });

        spinnerBrands.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedBrand = parent.getItemAtPosition(position).toString();
                viewModel.loadModels(selectedBrand);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerModels.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedModel = parent.getItemAtPosition(position).toString();
                String selectedBrand = spinnerBrands.getSelectedItem().toString();
                viewModel.loadVariantsAndYears(selectedBrand, selectedModel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }



    private void observeViewModel() {
        viewModel.getBrands().observe(this, brands ->
                SpinnerAdaptor.setupSpinner(this, spinnerBrands, brands));

        viewModel.getModels().observe(this, models ->
                SpinnerAdaptor.setupSpinner(this, spinnerModels, models));

        viewModel.getVariants().observe(this, variants ->
                SpinnerAdaptor.setupSpinner(this, spinnerVariants, variants));

        viewModel.getYears().observe(this, years ->
                SpinnerAdaptor.setupSpinner(this, spinnerYears, years));
    }
}
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finpro.Adaptor.ServiceTypeAdaptor;
import com.example.finpro.Domain.ServiceTypeDomain;
import com.example.finpro.R;
import com.example.finpro.Viewmodel.ServiceTypeViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

public class ServiceType extends AppCompatActivity implements ServiceTypeAdaptor.OnServiceSelectedListener {
    private TextView onsiteServicesText, priceOnsite;
    private ServiceTypeViewModel viewModel;
    private TextView next;
    private ServiceTypeAdaptor adapter;
    private TextView priceEstimation;
    private ImageView addOnsiteService;
    private RecyclerView recyclerViewType;
    private boolean isOnsiteSelected = false;
    private long totalPrice = 0;
    private View homeBtn, profileBtn, inboxBtn, activityBtn;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_type);

        viewModel = new ViewModelProvider(this).get(ServiceTypeViewModel.class);

        onsiteServicesText = findViewById(R.id.onsiteServicesText);
        priceOnsite = findViewById(R.id.priceOnsite);
        back = findViewById(R.id.back);
        activityBtn = findViewById(R.id.activityBtn);
        profileBtn = findViewById(R.id.profileBtn);
        inboxBtn = findViewById(R.id.inboxBtn);
        homeBtn = findViewById(R.id.homeBtn);

        String serviceCategory = getIntent().getStringExtra("serviceCategory");
        viewModel.setServiceCategory(serviceCategory);

        // Setup back button
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // This will go back to the previous activity
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceType.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Set onClickListener for profileBtn
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceType.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        // Set onClickListener for inboxBtn
        inboxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceType.this, ChatActivity.class); // Navigate to ChatActivity
                startActivity(intent);
            }
        });

        // Setup activity button
        activityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser == null) {
                    Toast.makeText(ServiceType.this, "Please login first", Toast.LENGTH_SHORT).show();
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

                                        Intent intent = new Intent(ServiceType.this, HomeServiceActivity.class);
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
                                    Toast.makeText(ServiceType.this,
                                            "No active orders found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(ServiceType.this,
                                        "Error fetching orders: " + databaseError.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        next = findViewById(R.id.Next);
        next.setOnClickListener(view -> {
            Intent intent = new Intent(ServiceType.this, BookingServices.class);
            String serviceType;
            if (isOnsiteSelected) {
                serviceType = onsiteServicesText.getText().toString();
            } else {
                serviceType = adapter.getSelectedServices();
            }
            intent.putExtra("serviceType", serviceType);
            intent.putExtra("priceEstimation", priceEstimation.getText().toString());
            intent.putExtras(getIntent().getExtras());
            intent.putExtra("serviceCategory", serviceCategory);
            startActivity(intent);
        });

        setupViews();
        setupRecyclerView();
        observeViewModel();

        String vehicleType = getIntent().getStringExtra("vehicleType");
        if (vehicleType != null) {
            viewModel.setVehicleType(vehicleType);
        }
    }

    private void setupViews() {
        priceEstimation = findViewById(R.id.priceEstimation);
        addOnsiteService = findViewById(R.id.addOnsiteSerivce);
        addOnsiteService.setImageResource(R.drawable.add_circle);

        addOnsiteService.setOnClickListener(v -> {
            isOnsiteSelected = !isOnsiteSelected;
            addOnsiteService.setImageResource(isOnsiteSelected ?
                    R.drawable.check_circle : R.drawable.add_circle);
            adapter.setSelectionEnabled(!isOnsiteSelected);

            if (isOnsiteSelected) {
                // Clear other selections and update total
                totalPrice = Long.parseLong(viewModel.getOnsiteService().getValue().getOnsitePrice()
                        .replace("Rp ", "").replace(",", ""));
            } else {
                totalPrice = 0;
            }
            updatePriceEstimation();
        });
    }

    private void setupRecyclerView() {
        recyclerViewType = findViewById(R.id.recyclerViewType);
        recyclerViewType.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ServiceTypeAdaptor(new ArrayList<>(), this);
        recyclerViewType.setAdapter(adapter);
    }


    @Override
    public void onServiceSelected(ServiceTypeDomain service, boolean isSelected) {
        long servicePrice = Long.parseLong(service.getServicePrice()
                .replace("Rp ", "").replace(",", ""));

        if (isSelected) {
            totalPrice += servicePrice;
        } else {
            totalPrice -= servicePrice;
        }
        updatePriceEstimation();
    }

    private void updatePriceEstimation() {
        priceEstimation.setText(String.format("Rp %,d", totalPrice));
    }
    private void observeViewModel() {
        viewModel.getServices().observe(this, services -> {
            if (services != null) {
                adapter.updateServices(services);
            }
        });

        viewModel.getOnsiteService().observe(this, onsiteService -> {
            if (onsiteService != null) {
                onsiteServicesText.setText(onsiteService.getOnsiteName());
                priceOnsite.setText(onsiteService.getOnsitePrice());

                if (isOnsiteSelected) {
                    totalPrice = Long.parseLong(onsiteService.getOnsitePrice()
                            .replace("Rp ", "").replace(",", ""));
                    updatePriceEstimation();
                }
            }
        });
    }

}



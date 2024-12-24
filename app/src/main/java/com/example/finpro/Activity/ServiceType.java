package com.example.finpro.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finpro.Adaptor.ServiceTypeAdaptor;
import com.example.finpro.Domain.ServiceTypeDomain;
import com.example.finpro.R;
import com.example.finpro.Viewmodel.ServiceTypeViewModel;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_type);

        viewModel = new ViewModelProvider(this).get(ServiceTypeViewModel.class);

        onsiteServicesText = findViewById(R.id.onsiteServicesText);
        priceOnsite = findViewById(R.id.priceOnsite);

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



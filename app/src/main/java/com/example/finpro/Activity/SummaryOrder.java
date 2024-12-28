package com.example.finpro.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.finpro.R;
import com.example.finpro.Viewmodel.SummaryOrderViewModel;

public class SummaryOrder extends AppCompatActivity {
    private TextView bookOrHome;
    private TextView next;
    private TextView vehicleTypeText, brandText, modelText, variantText, yearText;
    private TextView serviceTypeText, dateText, timeText, priceEstimationText;
    private SummaryOrderViewModel viewModel;
    private ImageView vehicleImage;
    private TextView addressLabel;
    private TextView addressValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_summary_order);

        initializeViews();
        setupViewModel();
        setupClickListeners();
        observeViewModel();
    }

    private void initializeViews() {
        addressLabel = findViewById(R.id.textView14);
        addressValue = findViewById(R.id.textView15);
        bookOrHome = findViewById(R.id.bookOrHome);
        next = findViewById(R.id.Next);
        vehicleImage = findViewById(R.id.vehicleImage);
        vehicleTypeText = findViewById(R.id.vehicleTypeText);
        brandText = findViewById(R.id.brandText);
        modelText = findViewById(R.id.modelText);
        variantText = findViewById(R.id.variantText);
        yearText = findViewById(R.id.yearText);
        serviceTypeText = findViewById(R.id.serviceTypeText);
        dateText = findViewById(R.id.dateText);
        timeText = findViewById(R.id.timeText);
        priceEstimationText = findViewById(R.id.priceEstimationText);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(SummaryOrderViewModel.class);

        String serviceCategory = getIntent().getStringExtra("serviceCategory");
        boolean isSelfService = getIntent().getBooleanExtra("isSelfService", false);

        if ("bookServices".equals(serviceCategory) && isSelfService) {
            // Hide address fields for self-service bookings
            addressLabel.setVisibility(View.GONE);
            addressValue.setVisibility(View.GONE);
        } else {
            // Show address fields for home service or pickup service
            addressLabel.setVisibility(View.VISIBLE);
            addressValue.setVisibility(View.VISIBLE);
        }

        // Get data from intent extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            viewModel.setVehicleType(extras.getString("vehicleType", ""));
            viewModel.setBrand(extras.getString("brand", ""));
            viewModel.setModel(extras.getString("model", ""));
            viewModel.setVariant(extras.getString("variant", ""));
            viewModel.setYear(extras.getString("year", ""));
            viewModel.setServiceType(extras.getString("serviceType", ""));
            viewModel.setBookingDate(extras.getString("bookingDate", ""));
            viewModel.setBookingTime(extras.getString("bookingTime", ""));
            viewModel.setPriceEstimation(extras.getString("priceEstimation", ""));
        }

        if (extras != null) {
            viewModel.setServiceCategory(extras.getString("serviceCategory", ""));
        }
    }

    private void setupClickListeners() {
        next.setOnClickListener(view -> {
            Intent intent = new Intent(SummaryOrder.this, OrderSucced.class);
            startActivity(intent);
        });
    }

    private void observeViewModel() {
        viewModel.getVehicleType().observe(this, value -> vehicleTypeText.setText(value));
        viewModel.getBrand().observe(this, value -> brandText.setText(value));
        viewModel.getModel().observe(this, value -> modelText.setText(value));
        viewModel.getVehicleType().observe(this, type -> {
            vehicleImage.setImageResource(
                    "car".equals(type.toLowerCase()) ?
                            R.drawable.mobil : R.drawable.moge_summary
            );
        });
        viewModel.getVariant().observe(this, value -> variantText.setText(value));
        viewModel.getYear().observe(this, value -> yearText.setText(value));
        viewModel.getServiceType().observe(this, value -> serviceTypeText.setText(value));
        viewModel.getBookingDate().observe(this, value -> dateText.setText(value));
        viewModel.getBookingTime().observe(this, value -> timeText.setText(value));
        viewModel.getPriceEstimation().observe(this, value -> priceEstimationText.setText(value));
        viewModel.getServiceCategory().observe(this, category -> {
            String displayText = "bookServices".equals(category) ?
                    "Booking Service" : "Home Service";
            bookOrHome.setText(displayText);
        });
    }
}

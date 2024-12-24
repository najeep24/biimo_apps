package com.example.finpro.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.finpro.Adaptor.SpinnerHomeAdaptor;
import com.example.finpro.R;
import com.example.finpro.Viewmodel.DetailInformationHomeViewModel;

public class DetailHomeServices extends AppCompatActivity {
    private TextView next;
    private Spinner spinnerBrands, spinnerModels, spinnerVariants, spinnerYears;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private DetailInformationHomeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_information_home);

        viewModel = new ViewModelProvider(this).get(DetailInformationHomeViewModel.class);
        initializeViews();
        setupListeners();
        observeViewModel();

        // Load initial data
        viewModel.loadBrands();
    }

    private void initializeViews() {
        next = findViewById(R.id.Next);
        radioGroup = findViewById(R.id.RadioGroupSelectHome);
        spinnerBrands = findViewById(R.id.spinner_brandsHome);
        spinnerModels = findViewById(R.id.spinner_modelsHome);
        spinnerVariants = findViewById(R.id.spinner_variantsHome);
        spinnerYears = findViewById(R.id.spinner_yearsHome);
        radioButton = findViewById(R.id.radioBtn_mobilHome);

        radioGroup.check(R.id.radioBtn_motorHome);
        radioButton.setEnabled(false);
    }

    private void setupListeners() {
        next.setOnClickListener(v -> {
            Intent intent = new Intent(DetailHomeServices.this, ServiceType.class);
            startActivity(intent);
        });

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String vehicleType = (checkedId == R.id.radioBtn_motorHome) ? "motorcycles" : "cars";
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
                SpinnerHomeAdaptor.setupSpinner(this, spinnerBrands, brands));

        viewModel.getModels().observe(this, models ->
                SpinnerHomeAdaptor.setupSpinner(this, spinnerModels, models));

        viewModel.getVariants().observe(this, variants ->
                SpinnerHomeAdaptor.setupSpinner(this, spinnerVariants, variants));

        viewModel.getYears().observe(this, years ->
                SpinnerHomeAdaptor.setupSpinner(this, spinnerYears, years));
    }
}
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

import com.example.finpro.Adaptor.SpinnerAdaptor;
import com.example.finpro.R;
import com.example.finpro.Viewmodel.DetailInformationViewModel;

import androidx.lifecycle.ViewModelProvider;


public class Detail_information extends AppCompatActivity {
    private TextView next;
    private Spinner spinnerBrands, spinnerModels, spinnerVariants, spinnerYears;
    private RadioGroup radioGroup;
    private RadioButton carButton, motorcycleButton;
    private DetailInformationViewModel viewModel;

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
            startActivity(intent);
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
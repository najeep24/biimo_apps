package com.example.finpro.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.lifecycle.ViewModelProvider;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finpro.Adaptor.DateAdaptor;
import com.example.finpro.Adaptor.TimeBookAdaptor;
import com.example.finpro.Domain.DateDomain;
import com.example.finpro.R;
import com.example.finpro.Viewmodel.BookingViewModel;
import java.util.ArrayList;


public class BookingServices extends AppCompatActivity implements DateAdaptor.OnDateClickListener {
    private TextView next;
    private BookingViewModel viewModel;
    private DateAdaptor dateAdapter;
    private TimeBookAdaptor timeBookAdapter;
    private RecyclerView recyclerViewDate;
    private RecyclerView recyclerViewTime;
    private ConstraintLayout mapsLayout;
    private RadioGroup radioGroup;
    private String serviceCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_services);

        serviceCategory = getIntent().getStringExtra("serviceCategory");
        initializeViews();
        setupViewModel();
        setupRecyclerViews();
        setupLayoutBasedOnServiceType();
        setupClickListeners();
        observeViewModelData();

        // Load initial booking dates
        viewModel.fetchBookingDates("2024", "12");
    }

    private void initializeViews() {
        next = findViewById(R.id.Next);
        radioGroup = findViewById(R.id.radioGroupBs);
        mapsLayout = findViewById(R.id.MapsLayout);
        recyclerViewDate = findViewById(R.id.RecyclerViewBs);
        recyclerViewTime = findViewById(R.id.RecyclerViewTimeBook);
    }

    private void setupLayoutBasedOnServiceType() {
        if ("homeServices".equals(serviceCategory)) {
            // For home services, hide radio group and always show maps
            radioGroup.setVisibility(View.GONE);
            mapsLayout.setVisibility(View.VISIBLE);
        } else {
            // For book services, show radio group and handle visibility
            radioGroup.setVisibility(View.VISIBLE);
            radioGroup.check(R.id.radioBtn_SelfService);
            mapsLayout.setVisibility(View.GONE);
        }
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(BookingViewModel.class);
    }

    private void setupRecyclerViews() {
        // Setup date recycler view
        LinearLayoutManager dateLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewDate.setLayoutManager(dateLayoutManager);
        dateAdapter = new DateAdaptor(new ArrayList<>());
        dateAdapter.setOnDateClickListener(this);
        recyclerViewDate.setAdapter(dateAdapter);

        // Setup time recycler view
        LinearLayoutManager timeLayoutManager = new LinearLayoutManager(this);
        recyclerViewTime.setLayoutManager(timeLayoutManager);
        recyclerViewTime.setItemAnimator(new DefaultItemAnimator());
        recyclerViewTime.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        timeBookAdapter = new TimeBookAdaptor(this, new ArrayList<>());
        recyclerViewTime.setAdapter(timeBookAdapter);
    }

    private void setupClickListeners() {
        if ("bookServices".equals(serviceCategory)) {
            radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                mapsLayout.setVisibility(checkedId == R.id.radioBtn_pickUp ? View.VISIBLE : View.GONE);
            });
        }

        next.setOnClickListener(view -> {
            Intent intent = new Intent(BookingServices.this, SummaryOrder.class);

            // Add selected date and time
            DateDomain selectedDate = dateAdapter.getSelectedDate();
            String selectedTime = timeBookAdapter.getSelectedTime();

            if (selectedDate != null) {
                intent.putExtra("bookingDate", selectedDate.getDay() + ", " + selectedDate.getDate());
            }
            intent.putExtra("bookingTime", selectedTime);

            // Pass service type and delivery mode
            intent.putExtra("serviceCategory", serviceCategory);
            if ("bookServices".equals(serviceCategory)) {
                boolean isSelfService = radioGroup.getCheckedRadioButtonId() == R.id.radioBtn_SelfService;
                intent.putExtra("isSelfService", isSelfService);
            }

            // Pass through all previous data
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                intent.putExtras(extras);
            }

            startActivity(intent);
        });
    }

    private void observeViewModelData() {
        viewModel.getDates().observe(this, dates -> {
            dateAdapter.updateDates(dates);
        });

        viewModel.getTimeSlots().observe(this, timeSlots -> {
            timeBookAdapter.updateTimeSlots(timeSlots);
        });
    }

    @Override
    public void onDateClick(DateDomain date) {
        viewModel.fetchTimeSlots("2024", "12", date.getDate());
        dateAdapter.setSelectedDate(date);
    }
}

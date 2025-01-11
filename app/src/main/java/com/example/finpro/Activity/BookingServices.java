package com.example.finpro.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finpro.Adaptor.DateAdaptor;
import com.example.finpro.Adaptor.TimeBookAdaptor;
import com.example.finpro.Domain.DateDomain;
import com.example.finpro.R;
import com.example.finpro.Viewmodel.BookingDates;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BookingServices extends AppCompatActivity implements DateAdaptor.OnDateClickListener {

    private TextView next, dayBook, dateBook;
    private BookingDates viewModel;
    private DateAdaptor dateAdapter;
    private TimeBookAdaptor timeBookAdapter;
    private RecyclerView recyclerViewDate, recyclerViewTime;
    private ConstraintLayout mapsLayout;
    private RadioGroup radioGroup;
    private String serviceCategory;
    private View homeBtn, profileBtn, inboxBtn, activityBtn;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_services);

        serviceCategory = getIntent().getStringExtra("serviceCategory");
        Log.d("BookingServices", "Service Category: " + serviceCategory);

        initializeViews();
        setCurrentDayAndDate();
        setupViewModel();
        setupRecyclerViews();
        setupLayoutBasedOnServiceType();
        setupClickListeners();
        observeViewModelData();

        List<DateDomain> dates = generateNextSevenDays();
        viewModel.BookingDates(dates);
    }

    private void initializeViews() {
        next = findViewById(R.id.Next);
        dayBook = findViewById(R.id.dayBook);
        dateBook = findViewById(R.id.dateBook);
        radioGroup = findViewById(R.id.radioGroupBs);
        mapsLayout = findViewById(R.id.MapsLayout);
        recyclerViewDate = findViewById(R.id.RecyclerViewBs);
        recyclerViewTime = findViewById(R.id.RecyclerViewTimeBook);
        profileBtn = findViewById(R.id.profileBtn);
        inboxBtn = findViewById(R.id.inboxBtn);
        activityBtn = findViewById(R.id.activityBtn);
        homeBtn = findViewById(R.id.homeBtn);
        back = findViewById(R.id.back);
    }

    private void setCurrentDayAndDate() {
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        String currentDay = dayOfWeek.toString().substring(0, 1).toUpperCase() +
                dayOfWeek.toString().substring(1).toLowerCase();
        String currentDate = String.valueOf(today.getDayOfMonth());

        if (dayBook != null) {
            dayBook.setText(currentDay);
        } else {
            Log.e("BookingServices", "dayBook TextView is null");
        }

        if (dateBook != null) {
            dateBook.setText(currentDate);
        } else {
            Log.e("BookingServices", "dateBook TextView is null");
        }
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(BookingDates.class);
    }

    private void setupRecyclerViews() {
        LinearLayoutManager dateLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewDate.setLayoutManager(dateLayoutManager);
        dateAdapter = new DateAdaptor(new ArrayList<>());
        dateAdapter.setOnDateClickListener(this);
        recyclerViewDate.setAdapter(dateAdapter);

        LinearLayoutManager timeLayoutManager = new LinearLayoutManager(this);
        recyclerViewTime.setLayoutManager(timeLayoutManager);
        recyclerViewTime.setItemAnimator(new DefaultItemAnimator());
        recyclerViewTime.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        timeBookAdapter = new TimeBookAdaptor(this, new ArrayList<>());
        recyclerViewTime.setAdapter(timeBookAdapter);
    }

    private void setupLayoutBasedOnServiceType() {
        if ("homeServices".equals(serviceCategory)) {
            radioGroup.setVisibility(View.GONE);
            mapsLayout.setVisibility(View.VISIBLE);
            Log.d("BookingServices", "Home services selected: RadioGroup GONE, MapsLayout VISIBLE");
        } else {
            radioGroup.setVisibility(View.VISIBLE);
            radioGroup.check(R.id.radioBtn_SelfService);
            mapsLayout.setVisibility(View.GONE);
            Log.d("BookingServices", "Book services selected: RadioGroup VISIBLE, MapsLayout GONE");
        }
    }

    private void setupClickListeners() {
        if ("bookServices".equals(serviceCategory)) {
            radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                if (checkedId == R.id.radioBtn_pickUp) {
                    mapsLayout.setVisibility(View.VISIBLE);
                    findViewById(R.id.pickupAddress).setVisibility(View.VISIBLE);
                } else {
                    mapsLayout.setVisibility(View.GONE);
                    findViewById(R.id.pickupAddress).setVisibility(View.GONE);
                }
            });
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // This will go back to the previous activity
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingServices.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Set onClickListener for profileBtn
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingServices.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        // Set onClickListener for inboxBtn
        inboxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingServices.this, ChatActivity.class); // Navigate to ChatActivity
                startActivity(intent);
            }
        });

        activityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser == null) {
                    Toast.makeText(BookingServices.this, "Please login first", Toast.LENGTH_SHORT).show();
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

                                        Intent intent = new Intent(BookingServices.this, HomeServiceActivity.class);
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
                                    Toast.makeText(BookingServices.this,
                                            "No active orders found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(BookingServices.this,
                                        "Error fetching orders: " + databaseError.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        next.setOnClickListener(view -> {
            Intent intent = new Intent(BookingServices.this, SummaryOrder.class);

            DateDomain selectedDate = dateAdapter.getSelectedDate();
            String selectedTime = timeBookAdapter.getSelectedTime();

            if (selectedDate != null) {
                intent.putExtra("bookingDate", selectedDate.getFullDate());  // Full date for storage
                intent.putExtra("displayDate", selectedDate.getDate());      // Short date for display
            }

            intent.putExtra("bookingTime", selectedTime);
            intent.putExtra("serviceCategory", serviceCategory);

            if ("bookServices".equals(serviceCategory)) {
                boolean isSelfService = radioGroup.getCheckedRadioButtonId() == R.id.radioBtn_SelfService;
                intent.putExtra("isSelfService", isSelfService);

                if (radioGroup.getCheckedRadioButtonId() == R.id.radioBtn_pickUp) {
                    String address = ((EditText) findViewById(R.id.pickupAddress)).getText().toString();
                    intent.putExtra("pickupAddress", address);
                }
            }

            if ("homeServices".equals(serviceCategory)) {
                String address = ((EditText) findViewById(R.id.pickupAddress)).getText().toString();
                intent.putExtra("pickupAddress", address);
                findViewById(R.id.pickupAddress).setVisibility(View.VISIBLE);
            }

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                intent.putExtras(extras);
            }

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            intent.putExtra("userId", userId);

            startActivity(intent);
        });
    }

    private void observeViewModelData() {
        viewModel.getDates().observe(this, dates -> {
            dateAdapter.updateDates(dates);
            if (!dates.isEmpty()) {
                dateAdapter.setSelectedDate(dates.get(0));
                viewModel.fetchTimeSlotsForDate(dates.get(0).getDate());
            }
        });

        viewModel.getTimeSlots().observe(this, timeSlots -> {
            timeBookAdapter.updateTimeSlots(timeSlots);
            if (!timeSlots.isEmpty()) {
                timeBookAdapter.selectFirstTimeSlot();
            }
        });
    }

    private List<DateDomain> generateNextSevenDays() {
        List<DateDomain> dates = new ArrayList<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEEE");
        DateTimeFormatter shortDateFormatter = DateTimeFormatter.ofPattern("d");
        DateTimeFormatter fullDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = today.plusDays(i);
            String day = currentDate.format(dayFormatter);
            String shortDate = currentDate.format(shortDateFormatter);
            String fullDate = currentDate.format(fullDateFormatter);
            boolean isHoliday = (currentDate.getDayOfWeek() == DayOfWeek.SATURDAY ||
                    currentDate.getDayOfWeek() == DayOfWeek.SUNDAY);

            DateDomain dateDomain = new DateDomain(day, shortDate, "Available");
            dateDomain.setFullDate(fullDate);
            dateDomain.setHoliday(isHoliday);

            Log.d("BookingServices", "Generated date: " + day + " " + shortDate + " " + fullDate);

            dates.add(dateDomain);
        }

        return dates;
    }

    @Override
    public void onDateClick(DateDomain date) {
        viewModel.fetchTimeSlotsForDate(date.getDate());
        dateAdapter.setSelectedDate(date);
    }
}
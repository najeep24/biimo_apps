package com.example.finpro.Viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.finpro.Domain.DateDomain;
import com.example.finpro.Domain.TimeBookDomain;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class BookingViewModel extends ViewModel {
    private final MutableLiveData<List<DateDomain>> dates = new MutableLiveData<>();
    private final MutableLiveData<List<TimeBookDomain>> timeSlots = new MutableLiveData<>();
    private final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("bookingSlots");

    public LiveData<List<DateDomain>> getDates() {
        return dates;
    }

    public LiveData<List<TimeBookDomain>> getTimeSlots() {
        return timeSlots;
    }

    public void fetchBookingDates(String year, String month) {
        dbRef.child(year).child(month).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<DateDomain> dateList = new ArrayList<>();

                for (DataSnapshot dateSnapshot : dataSnapshot.getChildren()) {
                    String date = dateSnapshot.getKey();
                    String dayName = dateSnapshot.child("dayName").getValue(String.class);
                    Boolean isHoliday = dateSnapshot.child("isHoliday").getValue(Boolean.class);

                    String status = (isHoliday != null && isHoliday) ? "Not Available" : "Available";
                    dateList.add(new DateDomain(dayName, date, status));
                }
                dates.setValue(dateList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error
            }
        });
    }

    public void fetchTimeSlots(String year, String month, String date) {
        dbRef.child(year).child(month).child(date).child("timeSlots")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<TimeBookDomain> timeSlotList = new ArrayList<>();

                        for (DataSnapshot timeSlotSnapshot : dataSnapshot.getChildren()) {
                            String time = timeSlotSnapshot.getKey();
                            Boolean available = timeSlotSnapshot.child("available").getValue(Boolean.class);

                            // Only add available time slots
                            if (available != null && available) {
                                timeSlotList.add(new TimeBookDomain(time, false));
                            }
                        }
                        timeSlots.setValue(timeSlotList);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Handle error
                    }
                });
    }
}
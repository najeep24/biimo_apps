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
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingViewModel extends ViewModel {
    private final MutableLiveData<List<DateDomain>> dates = new MutableLiveData<>();
    private final MutableLiveData<List<TimeBookDomain>> timeSlots = new MutableLiveData<>();
    private final MutableLiveData<DateDomain> firstAvailableDate = new MutableLiveData<>();
    private final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("bookingSlots");

    public LiveData<List<DateDomain>> getDates() {
        return dates;
    }

    public LiveData<List<TimeBookDomain>> getTimeSlots() {
        return timeSlots;
    }

    public LiveData<DateDomain> getFirstAvailableDate() {
        return firstAvailableDate;
    }

    public void fetchBookingDates(String year, String month) {
        dbRef.child(year).child(month).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<DateDomain> dateList = new ArrayList<>();
                DateDomain firstAvailable = null;

                for (DataSnapshot dateSnapshot : dataSnapshot.getChildren()) {
                    String date = dateSnapshot.getKey();
                    String dayName = dateSnapshot.child("dayName").getValue(String.class);
                    Boolean isHoliday = dateSnapshot.child("isHoliday").getValue(Boolean.class);

                    String status = (isHoliday != null && isHoliday) ? "Not Available" : "Available";
                    DateDomain dateDomain = new DateDomain(dayName, date, status);
                    dateList.add(dateDomain);

                    // Find first available date
                    if (firstAvailable == null && "Available".equals(status)) {
                        firstAvailable = dateDomain;
                    }
                }

                dates.setValue(dateList);
                if (firstAvailable != null) {
                    firstAvailableDate.setValue(firstAvailable);
                    // Automatically fetch time slots for first available date
                    fetchTimeSlots(year, month, firstAvailable.getDate());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error
            }
        });
    }

    public void saveBookingTimestamp(String orderId) {
        DatabaseReference bookingRef = FirebaseDatabase.getInstance()
                .getReference("bookings")
                .child(orderId);

        Map<String, Object> timestampUpdate = new HashMap<>();
        timestampUpdate.put("timestamp", ServerValue.TIMESTAMP);

        bookingRef.updateChildren(timestampUpdate);
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

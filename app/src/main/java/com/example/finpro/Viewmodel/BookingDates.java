package com.example.finpro.Viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finpro.Domain.DateDomain;
import com.example.finpro.Domain.TimeBookDomain;

import java.util.ArrayList;
import java.util.List;

public class BookingDates extends ViewModel {

    private final MutableLiveData<List<DateDomain>> dates = new MutableLiveData<>();
    private final MutableLiveData<List<TimeBookDomain>> timeSlots = new MutableLiveData<>();

    public BookingDates() {
        dates.setValue(new ArrayList<>());
        timeSlots.setValue(new ArrayList<>());
    }

    // Set booking dates
    public void BookingDates(List<DateDomain> dateList) {
        dates.setValue(dateList);
    }

    // Retrieve booking dates
    public LiveData<List<DateDomain>> getDates() {
        return dates;
    }

    // Fetch time slots for a specific date
    public void fetchTimeSlotsForDate(String date) {
        // Simulated time slot data
        List<TimeBookDomain> slots = new ArrayList<>();
        slots.add(new TimeBookDomain("9:00 AM", false));
        slots.add(new TimeBookDomain("11:00 AM", false));
        slots.add(new TimeBookDomain("1:00 PM", false));
        slots.add(new TimeBookDomain("3:00 PM", false));
        slots.add(new TimeBookDomain("5:00 PM", false));

        // Update time slots
        timeSlots.setValue(slots);
    }

    // Retrieve time slots
    public LiveData<List<TimeBookDomain>> getTimeSlots() {
        return timeSlots;
    }
}

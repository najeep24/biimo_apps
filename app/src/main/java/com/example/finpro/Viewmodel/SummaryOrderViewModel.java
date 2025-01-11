package com.example.finpro.Viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.HashMap;
import java.util.Map;

public class SummaryOrderViewModel extends ViewModel {
    private MutableLiveData<String> serviceCategory = new MutableLiveData<>();
    private MutableLiveData<String> vehicleType = new MutableLiveData<>();
    private MutableLiveData<String> brand = new MutableLiveData<>();
    private MutableLiveData<String> model = new MutableLiveData<>();
    private MutableLiveData<String> variant = new MutableLiveData<>();
    private MutableLiveData<String> year = new MutableLiveData<>();
    private MutableLiveData<String> serviceType = new MutableLiveData<>();
    private MutableLiveData<String> bookingDate = new MutableLiveData<>();
    private MutableLiveData<String> bookingTime = new MutableLiveData<>();
    private MutableLiveData<String> priceEstimation = new MutableLiveData<>();
    private MutableLiveData<String> address = new MutableLiveData<>();

    // Getters
    public LiveData<String> getServiceCategory() { return serviceCategory; }
    public LiveData<String> getVehicleType() { return vehicleType; }
    public LiveData<String> getBrand() { return brand; }
    public LiveData<String> getModel() { return model; }
    public LiveData<String> getVariant() { return variant; }
    public LiveData<String> getYear() { return year; }
    public LiveData<String> getServiceType() { return serviceType; }
    public LiveData<String> getBookingDate() { return bookingDate; }
    public LiveData<String> getBookingTime() { return bookingTime; }
    public LiveData<String> getPriceEstimation() { return priceEstimation; }
    public LiveData<String> getAddress() { return address; }

    // Setters
    public void setServiceCategory(String value) { serviceCategory.setValue(value); }
    public void setVehicleType(String value) { vehicleType.setValue(value); }
    public void setBrand(String value) { brand.setValue(value); }
    public void setModel(String value) { model.setValue(value); }
    public void setVariant(String value) { variant.setValue(value); }
    public void setYear(String value) { year.setValue(value); }
    public void setServiceType(String value) { serviceType.setValue(value); }
    public void setBookingTime(String value) { bookingTime.setValue(value); }
    public void setPriceEstimation(String value) { priceEstimation.setValue(value); }
    public void setAddress(String value) { address.setValue(value); }

    // Modified setBookingDate with proper formatting
    public void setBookingDate(String value) {
        try {
            if (value != null && !value.isEmpty()) {
                // Parse the input date
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date date = inputFormat.parse(value);

                if (date != null) {
                    // Format the date to the desired output format
                    SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault());
                    String formattedDate = outputFormat.format(date);
                    bookingDate.setValue(formattedDate);
                } else {
                    bookingDate.setValue("Invalid date");
                }
            } else {
                bookingDate.setValue("No date provided");
            }
        } catch (ParseException e) {
            e.printStackTrace();
            bookingDate.setValue(value); // Use original value if parsing fails
        }
    }

    public void saveBookingTimestamp(String currentOrderId) {
        if (currentOrderId != null && !currentOrderId.isEmpty()) {
            Map<String, Object> timestampUpdate = new HashMap<>();
            timestampUpdate.put("timestamp", ServerValue.TIMESTAMP);

            FirebaseDatabase.getInstance()
                    .getReference("bookings")
                    .child(currentOrderId)
                    .updateChildren(timestampUpdate);
        }
    }
}
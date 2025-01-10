package com.example.finpro.Viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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

    // All existing getters remain the same
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
    public void setAddress(String value) { address.setValue(value); }

    // All other setters remain the same
    public void setServiceCategory(String value) { serviceCategory.setValue(value); }
    public void setVehicleType(String value) { vehicleType.setValue(value); }
    public void setBrand(String value) { brand.setValue(value); }
    public void setModel(String value) { model.setValue(value); }
    public void setVariant(String value) { variant.setValue(value); }
    public void setYear(String value) { year.setValue(value); }
    public void setServiceType(String value) { serviceType.setValue(value); }
    public void setBookingTime(String value) { bookingTime.setValue(value); }
    public void setPriceEstimation(String value) { priceEstimation.setValue(value); }

    // Modified setBookingDate with formatting
    public void setBookingDate(String value) {
        try {
            // Split the value if it's in "day, date" format
            String[] parts = value.split(",");
            int day = Integer.parseInt(parts[0].trim());
            int month = Integer.parseInt(parts[1].trim());

            // Create Calendar instance for current year
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);

            // Set the date
            calendar.set(year, month - 1, day); // month is 0-based in Calendar

            // Format the date
            SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault());
            String formattedDate = outputFormat.format(calendar.getTime());
            bookingDate.setValue(formattedDate);

        } catch (Exception e) {
            // Fallback to original value if parsing fails
            bookingDate.setValue(value);
        }
    }

    public void saveBookingTimestamp(String currentOrderId) {
    }
}

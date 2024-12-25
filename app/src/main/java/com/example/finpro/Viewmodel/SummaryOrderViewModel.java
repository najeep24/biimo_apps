package com.example.finpro.Viewmodel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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

    // Getters for LiveData
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

    // Setters for data
    public void setServiceCategory(String value) { serviceCategory.setValue(value); }
    public void setVehicleType(String value) { vehicleType.setValue(value); }
    public void setBrand(String value) { brand.setValue(value); }
    public void setModel(String value) { model.setValue(value); }
    public void setVariant(String value) { variant.setValue(value); }
    public void setYear(String value) { year.setValue(value); }
    public void setServiceType(String value) { serviceType.setValue(value); }
    public void setBookingDate(String value) { bookingDate.setValue(value); }
    public void setBookingTime(String value) { bookingTime.setValue(value); }
    public void setPriceEstimation(String value) { priceEstimation.setValue(value); }
}
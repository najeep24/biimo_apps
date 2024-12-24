package com.example.finpro.Domain;

public class ServiceTypeDomain {
    private String serviceName;
    private String servicePrice;
    private long priceValue; // Added to store numerical price
    private boolean isSelected;

    public ServiceTypeDomain(String serviceName, String servicePrice) {
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
        try {
            this.priceValue = Long.parseLong(servicePrice.replace("Rp ", "").replace(",", ""));
        } catch (NumberFormatException e) {
            this.priceValue = 0;
        }
        this.isSelected = false;
    }

    // Add getters and setters
    public long getPriceValue() {
        return priceValue;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getServiceName() {
        return serviceName;
    }
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServicePrice() {
        return servicePrice;
    }
    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }
}


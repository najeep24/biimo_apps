package com.example.finpro.Domain;

public class ServiceTypeDomain {
    private String serviceName;
    private String servicePrice;

    public ServiceTypeDomain(String serviceName, String servicePrice){
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
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


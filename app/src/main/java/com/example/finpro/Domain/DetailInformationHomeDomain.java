package com.example.finpro.Domain;

public class DetailInformationHomeDomain {
    private String brand;
    private String model;
    private String variant;
    private String year;

    public DetailInformationHomeDomain(String brand, String model, String variant, String year) {
        this.brand = brand;
        this.model = model;
        this.variant = variant;
        this.year = year;
    }

    // Getters and Setters
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getVariant() { return variant; }
    public void setVariant(String variant) { this.variant = variant; }
    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }
}

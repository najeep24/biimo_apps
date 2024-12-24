package com.example.finpro.Domain;

public class OnSiteServiceDomain {
    private String onsiteName;
    private String onsitePrice;

    public OnSiteServiceDomain(String name, String price) {
        this.onsiteName = name;
        this.onsitePrice = price;
    }

    public String getOnsiteName() { return onsiteName; }
    public String getOnsitePrice() { return onsitePrice; }
}

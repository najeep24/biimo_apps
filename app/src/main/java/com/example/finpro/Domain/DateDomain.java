package com.example.finpro.Domain;

public class DateDomain {
    private String day;
    private String date;
    private String status;

    public DateDomain(String day, String date, String status){
        this.day = day;
        this.date = date;
        this.status = status;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

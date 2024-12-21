package com.example.finpro.Domain;

public class TimeBookDomain{

    private String time;
    private boolean isSelected;

    public TimeBookDomain(String time, boolean isSelected){
        this.time = time;
        this.isSelected = isSelected;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    @Override
    public String toString(){
        return "TimeBookDomain{" +
                "name='" + time + '\''+
                ", isSelected=" + isSelected +
                '}';
    }
}

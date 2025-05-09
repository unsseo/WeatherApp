package com.example.weatherapp;

public class WeatherData {
    private String day;
    private String tempLow;
    private String tempHigh;
    private int iconResId;

    public WeatherData(String day, String tempLow, String tempHigh, int iconResId) {
        this.day = day;
        this.tempLow = tempLow;
        this.tempHigh = tempHigh;
        this.iconResId = iconResId;
    }

    public String getDay() { return day; }
    public String getTempLow() { return tempLow; }
    public String getTempHigh() { return tempHigh; }
    public int getIconResId() { return iconResId; }
}

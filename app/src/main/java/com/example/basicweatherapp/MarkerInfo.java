package com.example.basicweatherapp;

public class MarkerInfo {
    private double latitude;
    private double longitude;
    private String[] textsToDisplay; // 예: {"123", "단위"} 또는 {"중요도", "높음"}

    public MarkerInfo(double latitude, double longitude, String... textsToDisplay) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.textsToDisplay = textsToDisplay;
    }

    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String[] getTextsToDisplay() { return textsToDisplay; }
}

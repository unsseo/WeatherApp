package com.example.basicweatherapp.model;

import com.google.gson.annotations.SerializedName;

public class Item {
    @SerializedName("stationName") // <-- 이 필드가 누락되었을 가능성이 높습니다.
    private String stationName;
    @SerializedName("dataTime")
    private String dataTime;
    @SerializedName("staticName")
    private String staticName;
    @SerializedName("pm10Value")
    private String pm10Value; // **미세먼지(PM10) 농도**
    @SerializedName("pm10Grade")
    private String pm10Grade; // **미세먼지(PM10) 등급**

    public String getDataTime(){
        return  dataTime;
    }

    public String getStaticName(){
        return staticName;
    }

    public String getPm10Value(){
        return pm10Value;
    }

    public  String getPm10Grade(){
        return pm10Grade;
    }

    public String getStationName() {return stationName;}
}

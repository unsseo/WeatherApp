package com.example.basicweatherapp.model;

import com.google.gson.annotations.SerializedName;

public class AirQualityResponse {
    @SerializedName("response")
    private ResponseWrapper response;
    public ResponseWrapper getResponse(){
        return response;
    }

    public void setResponse(ResponseWrapper response){
        this.response= response;
    }
}

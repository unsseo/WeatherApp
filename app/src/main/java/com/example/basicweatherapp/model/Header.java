package com.example.basicweatherapp.model;

import com.google.gson.annotations.SerializedName;

public class Header {
    @SerializedName("resultCode")
    private String resultCode;
    @SerializedName("resultMsg")
    private String resultMsg;

    public String getResultCode(){
        return resultCode;
    }

    public void setResultCode(String resultCode){
        this.resultCode = resultCode;
    }

    public String getResultMsg(){
        return resultMsg;
    }

    public void setResultMsg(String resultMsg){
        this.resultMsg = resultMsg;
    }
}

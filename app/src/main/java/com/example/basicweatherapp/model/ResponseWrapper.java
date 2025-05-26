package com.example.basicweatherapp.model;

import com.google.gson.annotations.SerializedName;

public class ResponseWrapper {
    @SerializedName("header")
    private Header header;
    @SerializedName("body")
    private Body body;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
}
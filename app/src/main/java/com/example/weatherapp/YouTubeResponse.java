package com.example.weatherapp;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class YouTubeResponse {

    @SerializedName("items")
    private List<YouTubeVideo> items;

    public List<YouTubeVideo> getItems() {
        return items;
    }

    public void setItems(List<YouTubeVideo> items) {
        this.items = items;
    }
}

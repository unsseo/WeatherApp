package com.example.weatherapp;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class YouTubeSearchResponse {


    @SerializedName("items")
    private List<Item> items;  // ✅ 이 필드가 없어서 에러가 난 거야

    public List<Item> getItems() {
        return items;
    }

    public static class Item {
        @SerializedName("id")
        private Id id;

        public Id getId() {
            return id;
        }
    }

    public static class Id {
        @SerializedName("videoId")
        private String videoId;

        public String getVideoId() {
            return videoId;
        }
    }

}

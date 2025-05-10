package com.example.weatherapp;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class YouTubeSearchResponse {
    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public static class Item {
        private Id id;
        private Snippet snippet;

        public Id getId() {
            return id;
        }

        public static class Id {
            private String videoId;
            public String getVideoId() {
                return videoId;
            }
        }

        public static class Snippet {
            private String title;
            // 기타 필요한 필드 추가
        }
    }
}

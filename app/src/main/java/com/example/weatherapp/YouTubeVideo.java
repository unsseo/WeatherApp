package com.example.weatherapp;

import com.google.gson.annotations.SerializedName;


public class YouTubeVideo {
    @SerializedName("snippet")
    private Snippet snippet;

    public Snippet getSnippet() {
        return snippet;
    }

    public void setSnippet(Snippet snippet) {
        this.snippet = snippet;
    }

    public static class Snippet {
        @SerializedName("title")
        private String title;

        @SerializedName("description")
        private String description;

        @SerializedName("channelTitle")
        private String channelTitle;

        @SerializedName("publishedAt")
        private String publishedAt;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getChannelTitle() {
            return channelTitle;
        }

        public void setChannelTitle(String channelTitle) {
            this.channelTitle = channelTitle;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }
    }
}

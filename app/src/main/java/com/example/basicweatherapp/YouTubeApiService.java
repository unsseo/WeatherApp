package com.example.basicweatherapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YouTubeApiService {


    @GET("youtube/v3/search")
    Call<YouTubeSearchResponse> searchVideos(@Query("q") String query);
}
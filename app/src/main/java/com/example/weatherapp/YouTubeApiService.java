package com.example.weatherapp;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface YouTubeApiService {
    @GET("youtube/v3/search")
    Call<YouTubeResponse> searchVideos(
            @Query("part") String part,
            @Query("q") String query,
            @Query("key") String apiKey,
            @Query("maxResults") int maxResults
    );
}

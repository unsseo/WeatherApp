package com.example.weatherapp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class YouTubeApiHandler {

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static final YouTubeApiService apiService = retrofit.create(YouTubeApiService.class);

    // 비디오 리스트를 전달할 인터페이스 (콜백) 정의
    public interface VideoListCallback {
        void onVideosLoaded(List<YouTubeVideo> videoList);
        void onFailure(String errorMessage);
    }

    // 비디오 검색 메서드
    public static void searchVideos(String query, final VideoListCallback callback) {
        Call<YouTubeResponse> call = apiService.searchVideos(
                "snippet",  // part
                query,      // 검색 쿼리
                "AIzaSyAU7f_gIf4-WTlf2d3cr1fz86nlrkzJ5bo",  // API 키
                10          // 최대 결과 수
        );

        // API 호출 비동기 처리
        call.enqueue(new Callback<YouTubeResponse>() {
            @Override
            public void onResponse(Call<YouTubeResponse> call, Response<YouTubeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onVideosLoaded(response.body().getItems());  // 비디오 리스트 전달
                } else {
                    callback.onFailure("Failed to load videos");
                }
            }

            @Override
            public void onFailure(Call<YouTubeResponse> call, Throwable t) {
                callback.onFailure(t.getMessage());  // 에러 메시지 전달
            }
        });
    }
}

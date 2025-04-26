package com.example.weatherapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private YouTubePlayerView youtubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        youtubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youtubePlayerView);  // 이것만 있으면 됨

        // 날씨 기반 유튜브 검색 실행
        getWeatherAndSearchVideos();
    }

    private void getWeatherAndSearchVideos() {
        // 날씨 정보를 임의의 데이터로 설정 (향후 API 연동 시 위치 기반 데이터 사용)
        String weatherCondition = "rain"; // 예: 비오는 날
        String searchKeyword = weatherCondition + " playlist"; // 키워드 생성

        // 백엔드 API로 검색
        fetchVideosFromBackend(searchKeyword);
    }

    private void fetchVideosFromBackend(String query) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")  // 백엔드 서버 URL (에뮬레이터에서 로컬 서버 접근용)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YouTubeApiService apiService = retrofit.create(YouTubeApiService.class);

        // 백엔드에 쿼리만 전달
        Call<YouTubeSearchResponse> call = apiService.searchVideos(query);  // 1개의 인자만 전달

        call.enqueue(new Callback<YouTubeSearchResponse>() {
            @Override
            public void onResponse(Call<YouTubeSearchResponse> call, Response<YouTubeSearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    YouTubeSearchResponse youtubeResponse = response.body();

                    // 응답 내용을 로그로 출력
                    Log.d("API Response", "Response Body: " + youtubeResponse.getItems());

                    if (youtubeResponse.getItems() != null && !youtubeResponse.getItems().isEmpty()) {
                        String videoId = youtubeResponse.getItems().get(0).getId().getVideoId();  // 수정된 부분
                        showVideo(videoId);
                    }
                } else {
                    Log.e("API Error", "Response was unsuccessful or body is null");
                }
            }

            @Override
            public void onFailure(Call<YouTubeSearchResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "백엔드 API 요청 실패", Toast.LENGTH_SHORT).show();
                Log.e("RetrofitError", "API 요청 실패", t);
            }
        });
    }


    private void showVideo(String videoId) {
        youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(videoId, 0);
            }
        });
    }

    // YouTube 링크로 이동하기
    private void openYouTubeLink(String videoId) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + videoId));
        startActivity(intent);
    }
}

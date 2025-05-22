package com.example.basicweatherapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class YoutubeActivity extends AppCompatActivity {
    private YouTubePlayerView youtubePlayerView;
    private Button btnOpenYoutube;
    private Button btnBack;
    private Button btnHome;
    private String selectedVideoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube);

        youtubePlayerView = findViewById(R.id.youtube_player_view);
        btnOpenYoutube = findViewById(R.id.btn_open_youtube);
        btnBack = findViewById(R.id.btn_back);
        btnHome = findViewById(R.id.btn_home);
        getLifecycle().addObserver(youtubePlayerView);

        btnOpenYoutube.setEnabled(false); // 초기엔 비활성화

        // WeatherWeekActivity에서 전달받은 날씨 타입 받기
        String weatherType = getIntent().getStringExtra("weather_type");
        if (weatherType == null) weatherType = "sunny"; // 기본값

        // 날씨 타입을 YouTube 검색 키워드로 변환 (필요시)
        String searchKeyword = weatherType + " playlist"; // 예: "rainy playlist"

        // YouTube 검색 실행
        fetchVideosFromBackend(searchKeyword);

        // 유튜브에서 열기 버튼 클릭 리스너
        btnOpenYoutube.setOnClickListener(v -> {
            if (selectedVideoId != null) {
                openYouTubeLink(selectedVideoId);
            } else {
                Toast.makeText(this, "비디오가 아직 준비되지 않았습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 이전 화면(WeatherWeekActivity)으로 돌아가기
        btnBack.setOnClickListener(v -> {
            finish(); // 현재 액티비티 종료 → 바로 이전 화면으로 이동
        });

        // 첫 화면(MainActivity)으로 이동
         btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(YoutubeActivity.this, HomeScreenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }

    private void fetchVideosFromBackend(String query) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);



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

                    // 1. 비디오만 추출
                    List<YouTubeSearchResponse.Item> videoItems = new ArrayList<>();
                    for (YouTubeSearchResponse.Item item : youtubeResponse.getItems()) {
                        if (item.getId() != null && item.getId().getVideoId() != null) {
                            videoItems.add(item);
                        }
                    }

                    // 2. 랜덤 선택
                    if (!videoItems.isEmpty()) {
                        Random random = new Random();
                        int randomIndex = random.nextInt(videoItems.size());
                        selectedVideoId = videoItems.get(randomIndex).getId().getVideoId();
                        btnOpenYoutube.setEnabled(true); // 버튼 활성화

                        // YouTubePlayerView에서 영상 미리보기
                        youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                            @Override
                            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                                youTubePlayer.loadVideo(selectedVideoId, 0);
                            }
                        });
                    } else {
                        Toast.makeText(YoutubeActivity.this, "검색 결과에 비디오가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("API Error", "Response was unsuccessful or body is null");
                }
            }

            @Override
            public void onFailure(Call<YouTubeSearchResponse>  call, Throwable t) {
                Toast.makeText(YoutubeActivity.this, "백엔드 API 요청 실패", Toast.LENGTH_SHORT).show();
                Log.e("RetrofitError", "API 요청 실패", t);
            }
        });
    }

    private void openYouTubeLink(String videoId) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v=" + videoId));
        startActivity(webIntent);
    }
}
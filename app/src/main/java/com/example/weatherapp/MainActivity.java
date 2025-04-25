package com.example.weatherapp;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private YouTubeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 비디오 검색 (예: "android development")
        YouTubeApiHandler.searchVideos("android development", new YouTubeApiHandler.VideoListCallback() {
            @Override
            public void onVideosLoaded(List<YouTubeVideo> videoList) {
                if (videoList == null || videoList.isEmpty()) {
                    Toast.makeText(MainActivity.this, "No videos found", Toast.LENGTH_SHORT).show();
                } else {
                    if (adapter == null) {
                        adapter = new YouTubeAdapter(videoList);
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                // 비디오 로드 실패 시
                Toast.makeText(MainActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

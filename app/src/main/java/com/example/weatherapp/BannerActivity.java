package com.example.weatherapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        // Android 13 이상 알림 권한 요청
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        // UI 요소 참조
        LinearLayout bannerLayout = findViewById(R.id.bannerLayout);
        ImageView weatherIcon = findViewById(R.id.weatherIcon);
        TextView weatherText = findViewById(R.id.weatherText);

        // NetworkCheckActivity에서 전달받은 오늘의 날씨 데이터 꺼내기
        Intent intent = getIntent();
        String weatherType = intent.getStringExtra("weather_type");


        // 배너 알림 표시
        UpdateWeatherBanner updater = new UpdateWeatherBanner();
        updater.updateWeatherBanner(weatherType, bannerLayout, weatherIcon, weatherText);
        updater.showWeatherNotification(this, weatherType);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                UpdateWeatherBanner updater = new UpdateWeatherBanner();
                Intent intent = getIntent();
                String weatherType = intent.getStringExtra("weather_type");
                updater.showWeatherNotification(this, weatherType != null ? weatherType : "sunny");
            }
        }
    }
}

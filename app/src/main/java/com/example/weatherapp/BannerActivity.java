package com.example.weatherapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

        String weatherType = getIntent().getStringExtra("weather_type");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                UpdateWeatherBanner updater = new UpdateWeatherBanner();
                String weatherType = getIntent().getStringExtra("weather_type");
                updater.showWeatherNotification(this, weatherType != null ? weatherType : "sunny");
            }
        }
    }
}

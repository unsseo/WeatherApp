package com.example.basicweatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeScreenActivity extends AppCompatActivity {

    private Button detailButton;
    private Button regionButton;
    private Button weeklyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);  // 홈화면 XML

        if (!NetworkUtils.isInternetAvailable(this)) {
            Intent intent = new Intent(this, NetworkCheckActivity.class);
            intent.putExtra("returnActivity", HomeScreenActivity.class.getName());
            startActivity(intent);
            finish();
            return;
        }
        //홈화면에서 해야 됨, 배너 알림
        String weatherType = WeatherUtils.getTodayWeatherType(this);
        // Android 13 이상 권한 체크 및 요청
        if (PermissionUtils.hasNotificationPermission(this)) {
            new UpdateWeatherBanner().showWeatherNotification(this, weatherType);
        } else {
            PermissionUtils.requestNotificationPermission(this);
        }


        detailButton = findViewById(R.id.detail_information);
        regionButton = findViewById(R.id.weather_region);
        weeklyButton = findViewById(R.id.weekly_weather);

        // 상세 정보 버튼 클릭 시 activity_details.xml로 이동
       /* detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this, DetailWindow.class);
                startActivity(intent);
            }
        }); */

        // 지역별 날씨 버튼 클릭 시 weather_map.xml로 이동
        regionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this, WeatherMap.class);
                startActivity(intent);
            }
        });

        // 주간 날씨 버튼 클릭 시 weather_week.xml로 이동
        weeklyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this, WeatherWeekActivity.class);
                startActivity(intent);
            }
        });
    }
}

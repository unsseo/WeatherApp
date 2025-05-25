package com.example.basicweatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

// LocationUtil import 추가 (패키지명에 맞게 수정)
import com.example.basicweatherapp.LocationUtil;

public class HomeScreenActivity extends AppCompatActivity {

    private Button detailButton;
    private Button regionButton;
    private Button weeklyButton;

    // 위치 정보 저장 변수 추가
    private double currentLatitude = 0.0;
    private double currentLongitude = 0.0;

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

        // ★ 위치 정보 받아오기
        LocationUtil.getCurrentLocation(this, new LocationUtil.LocationResultListener() {
            @Override
            public void onLocationResult(double latitude, double longitude) {
                currentLatitude = latitude;
                currentLongitude = longitude;
                // 필요 시 아래 Toast는 지워도 됨
                Toast.makeText(HomeScreenActivity.this, "위도: " + latitude + ", 경도: " + longitude, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLocationFailed(String message) {
                Toast.makeText(HomeScreenActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        // 상세 정보 버튼 클릭 시 activity_details.xml로 이동
        detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this, DetailWindow.class);
                startActivity(intent);
            }
        });

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

    // 권한 요청 결과 처리 (LocationUtil에서 권한 요청 시 필요)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) { // LocationUtil의 requestCode와 동일하게!
            if (grantResults.length > 0 && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                // 권한이 허용되면 다시 위치 요청
                LocationUtil.getCurrentLocation(this, new LocationUtil.LocationResultListener() {
                    @Override
                    public void onLocationResult(double latitude, double longitude) {
                        currentLatitude = latitude;
                        currentLongitude = longitude;
                    }
                    @Override
                    public void onLocationFailed(String message) {
                        Toast.makeText(HomeScreenActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

package com.example.basicweatherapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class HomeScreenActivity extends AppCompatActivity {

    private static final String TAG = "HomeScreenActivity";
    private Button detailButton, regionButton, weeklyButton;
    private double currentLatitude = 0.0, currentLongitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);

        // 1. 네트워크 체크
        if (!NetworkUtils.isInternetAvailable(this)) {
            startActivity(new Intent(this, NetworkCheckActivity.class)
                    .putExtra("returnActivity", HomeScreenActivity.class.getName()));
            finish();
            return;
        }


        if (isTaskRoot()) {
            checkNotificationPermission();

        }
        checkLocationPermission();

        // 4. 버튼 초기화
        initButtons();
    }


    private void checkNotificationPermission() {
        if (PermissionUtils.hasNotificationPermission(this)) {
            sendWeatherNotification();
            checkLocationPermission(); // 알림 권한 있으면 위치 체크
        } else {
            PermissionUtils.requestNotificationPermission(this);
        }
    }
    private void checkLocationPermission() {
        boolean hasFineLocation = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean hasCoarseLocation = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (!hasFineLocation && !hasCoarseLocation) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LocationUtil.LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            requestLocation();
        }
    }


    private void requestLocation() {
        LocationUtil.getCurrentLocation(this, new LocationUtil.LocationResultListener() {
            @Override
            public void onLocationResult(double latitude, double longitude) {
                currentLatitude = latitude;
                currentLongitude = longitude;
                Toast.makeText(HomeScreenActivity.this,
                        "위치 업데이트: " + latitude + ", " + longitude, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLocationFailed(String message) {
                Toast.makeText(HomeScreenActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initButtons() {
        detailButton = findViewById(R.id.detail_information);
        regionButton = findViewById(R.id.weather_region);
        weeklyButton = findViewById(R.id.weekly_weather);

        detailButton.setOnClickListener(v ->
                startActivity(new Intent(this, DetailWindow.class)));

        regionButton.setOnClickListener(v ->
                startActivity(new Intent(this, WeatherMap.class)));

        weeklyButton.setOnClickListener(v ->
                startActivity(new Intent(this, WeatherWeekActivity.class)));
    }

    private void sendWeatherNotification() {
        Log.d(TAG, "Sending weather notification");
        String weatherType = WeatherUtils.getTodayWeatherType(this);
        new UpdateWeatherBanner().showWeatherNotification(this, weatherType);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // 알림 권한 처리
        if (requestCode == PermissionUtils.NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendWeatherNotification();
            }
            // 알림 권한 처리 후 위치 권한 체크
            checkLocationPermission();
        }
        // 위치 권한 처리
        else if (requestCode == LocationUtil.LOCATION_PERMISSION_REQUEST_CODE) {
            boolean isAnyGranted = false;
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_GRANTED) {
                    isAnyGranted = true;
                    break;
                }
            }
            if (isAnyGranted) {
                requestLocation();
            } else {
                Toast.makeText(this, "위치 서비스 사용 불가", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

package com.example.basicweatherapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.widget.Toast;




public class WeatherWeekActivity extends AppCompatActivity {

    private String weatherType;
    private Button btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RecyclerView recyclerView;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_week);

        if (!NetworkUtils.isInternetAvailable(this)) {
            Intent intent = new Intent(this, NetworkCheckActivity.class);
            intent.putExtra("returnActivity", WeatherWeekActivity.class.getName());
            startActivity(intent);
            finish();
            return;
        }

        recyclerView = findViewById(R.id.recycler_view);
        btnBack = findViewById(R.id.btn_back);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String[] allDays = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        int todayIndex = getTodayIndex(allDays);
        List<String> orderedDays = getOrderedDays(allDays, todayIndex);

        int[] iconResIds = {
                R.drawable.ic_weather_sun,
                R.drawable.ic_weather_cloud,
                R.drawable.ic_weather_rain,
                R.drawable.ic_weather_snow,
                R.drawable.ic_weather_cloud,
                R.drawable.ic_weather_sun,
                R.drawable.ic_weather_rain
        };

        String[] tempLows  = {"14", "13", "11", "7", "18", "9", "12"};
        String[] tempHighs = {"20", "19", "17", "12", "25", "15", "18"};
        String c = getString(R.string.celcius);

        List<WeatherData> weekList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            int dayIdx = (todayIndex + i) % 7;
            weekList.add(new WeatherData(
                    orderedDays.get(i),
                    tempLows[dayIdx]+ c ,
                    tempHighs[dayIdx] + c ,
                    iconResIds[dayIdx]
            ));
        }
        WeatherAdapter adapter = new WeatherAdapter(weekList);
        recyclerView.setAdapter(adapter);

        // 오늘의 날씨 데이터 추출 (리스트 첫 번째가 오늘)
        WeatherData todayWeather = weekList.get(0);
        int todayIconResId = todayWeather.getIconResId();
        weatherType = getWeatherTypeFromIcon(todayIconResId);

        // 알림(배너)만 노출
        Intent bannerintent = new Intent(this, BannerActivity.class);
        bannerintent.putExtra("weather_type", weatherType); // "sunny", "cloudy" 등
        //startActivity(bannerintent);
        new UpdateWeatherBanner().showWeatherNotification(this, weatherType);

        Button btnOpenYoutube = findViewById(R.id.btn_open_youtube);
        btnOpenYoutube.setOnClickListener(v -> {
            Intent youtubeintent = new Intent(WeatherWeekActivity.this, YoutubeActivity.class);
            youtubeintent.putExtra("weather_type", weatherType); // 날씨 타입을 전달
            startActivity(youtubeintent);
        });

        //홈화면에서 해야 됨, 배너 알림

        // Android 13 이상 권한 체크 및 요청
        if (PermissionUtils.hasNotificationPermission(this)) {
            new UpdateWeatherBanner().showWeatherNotification(this, weatherType);
        } else {
            PermissionUtils.requestNotificationPermission(this);
        }


        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(WeatherWeekActivity.this, HomeScreenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtils.NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new UpdateWeatherBanner().showWeatherNotification(this, weatherType);
            } else {
                // 사용자가 권한을 거부했을 때의 처리
                Toast.makeText(this, "알림 표시 권한이 필요합니다", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private int getTodayIndex(String[] allDays) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.ENGLISH);
        String today = sdf.format(calendar.getTime());
        for (int i = 0; i < allDays.length; i++) {
            if (allDays[i].equals(today)) return i;
        }
        return 0;
    }

    private List<String> getOrderedDays(String[] allDays, int todayIndex) {
        List<String> ordered = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            int idx = (todayIndex + i) % 7;
            ordered.add(allDays[idx]);
        }
        return ordered;
    }

    // 아이콘 리소스 → 날씨 문자열 변환 함수
    private String getWeatherTypeFromIcon(int iconResId) {
        if (iconResId == R.drawable.ic_weather_sun) return "sunny";
        if (iconResId == R.drawable.ic_weather_cloud) return "cloudy";
        if (iconResId == R.drawable.ic_weather_rain) return "rainy";
        if (iconResId == R.drawable.ic_weather_snow) return "snow";
        return "sunny";
    }

}
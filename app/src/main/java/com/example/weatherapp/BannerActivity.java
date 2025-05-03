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

        setContentView(R.layout.activity_banner);  // 배너 레이아웃

        // Android 13 이상에서는 알림 권한 요청
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        // UI 요소 참조
        LinearLayout bannerLayout = findViewById(R.id.bannerLayout);
        ImageView weatherIcon = findViewById(R.id.weatherIcon);
        TextView weatherText = findViewById(R.id.weatherText);

        // 배너 UI 숨기기 (화면에 표시되지 않도록 설정)
        bannerLayout.setVisibility(LinearLayout.GONE);

        // 배너 알림 표시 (배너는 화면에는 나타내지 않지만 알림으로 표시)
        UpdateWeatherBanner updater = new UpdateWeatherBanner();
        updater.updateWeatherBanner("sunny", bannerLayout, weatherIcon, weatherText);
        updater.showWeatherNotification(this, "sunny");

        // 메인 화면 (week 화면)으로 이동
        Intent intent = new Intent(BannerActivity.this, WeekActivity.class);  // WeekActivity로 이동
        startActivity(intent);
        finish();  // 현재 배너 화면 종료
    }

    // 권한 요청 결과 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 허용됨
                UpdateWeatherBanner updater = new UpdateWeatherBanner();
                updater.showWeatherNotification(this, "sunny");
            }
        }
    }
}

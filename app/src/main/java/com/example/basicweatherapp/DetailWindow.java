package com.example.basicweatherapp;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailWindow extends AppCompatActivity {

    private Button dustCheckButton;
    private TextView textViewDate;

    // 위치 정보 변수 추가
    private double currentLatitude = 0.0;
    private double currentLongitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (!NetworkUtils.isInternetAvailable(this)) {
            Intent intent = new Intent(this, NetworkCheckActivity.class);
            intent.putExtra("returnActivity", DetailWindow.class.getName());
            startActivity(intent);
            finish();
            return;
        }

        // 위치 정보 받아오기
        LocationUtil.getCurrentLocation(this, new LocationUtil.LocationResultListener() {
            @Override
            public void onLocationResult(double latitude, double longitude) {
                currentLatitude = latitude;
                currentLongitude = longitude;
                Toast.makeText(DetailWindow.this,
                        "현재 위치\n위도: " + latitude + "\n경도: " + longitude,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLocationFailed(String message) {
                Toast.makeText(DetailWindow.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        dustCheckButton = findViewById(R.id.dust_check_button);
        textViewDate = findViewById(R.id.textView_date);
        Button homeButton = findViewById(R.id.home_button);

        String today = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREAN).format(new Date());
        textViewDate.setText(today);

        dustCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDustPopup();
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailWindow.this, HomeScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    // 위치 권한 처리 추가
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
                        Toast.makeText(DetailWindow.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDustPopup() {
        View popupView = getLayoutInflater().inflate(R.layout.dust_popup,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(popupView);
        final android.app.AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        popupView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        },6000);
    }
}

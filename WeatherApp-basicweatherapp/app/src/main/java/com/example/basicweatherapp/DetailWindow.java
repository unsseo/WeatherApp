package com.example.basicweatherapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DetailWindow extends AppCompatActivity {

    private Button dustCheckButton;
    private TextView textViewDate;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // 인터넷 연결 확인
        if (!NetworkUtils.isInternetAvailable(this)) {
            Intent intent = new Intent(this, NetworkCheckActivity.class);
            intent.putExtra("returnActivity", DetailWindow.class.getName());
            startActivity(intent);
            finish();
            return;
        }

        // 뷰 초기화
        dustCheckButton = findViewById(R.id.dust_check_button);
        textViewDate = findViewById(R.id.textView_date);
        Button homeButton = findViewById(R.id.home_button);

        // 오늘 날짜 설정
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
        textViewDate.setText(today);

        // 미세먼지 버튼 클릭 이벤트
        dustCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDustPopup(); //
            }
        });

        // 홈 버튼 클릭 이벤트
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

    private void showDustPopup() {
        LayoutInflater inflater = getLayoutInflater();
        View popupView = inflater.inflate(R.layout.dust_popup, null); // XML 연결

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(popupView);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

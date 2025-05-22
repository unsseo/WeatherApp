package com.example.basicweatherapp;

import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MainActivity extends Activity {

    private Button dustCheckButton;
    private TextView textViewDate;

    private Dialog dustDialog;
    private Handler handler = new Handler();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        dustCheckButton = findViewById(R.id.dust_check_button);
        textViewDate = findViewById(R.id.textView_date);

        // 오늘 날짜 세팅
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
        textViewDate.setText(today);

        dustCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDustPopup();
            }
        });
    }

    private void showDustPopup() {
        dustDialog = new Dialog(this);
        dustDialog.setContentView(R.layout.dust_popup);
        dustDialog.setCancelable(true);
        dustDialog.show();

        // 6초 후 팝업 자동 닫기
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dustDialog != null && dustDialog.isShowing()) {
                    dustDialog.dismiss();
                }
            }
        }, 6000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 액티비티 종료 시 핸들러 콜백 정리
        handler.removeCallbacksAndMessages(null);
        if (dustDialog != null && dustDialog.isShowing()) {
            dustDialog.dismiss();
        }
    }
}

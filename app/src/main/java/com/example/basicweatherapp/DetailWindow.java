package com.example.basicweatherapp;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DetailWindow extends AppCompatActivity {

    private Button dustCheckButton;
    private TextView textViewDate;

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

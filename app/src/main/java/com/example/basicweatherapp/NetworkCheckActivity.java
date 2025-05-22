package com.example.basicweatherapp;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NetworkCheckActivity extends AppCompatActivity {

    private TextView noInternetMessage;
    private String returnActivityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_check);

        // 복귀할 액티비티 이름 받아오기
        returnActivityName = getIntent().getStringExtra("returnActivity");

        Button retryButton = findViewById(R.id.retryButton);
        noInternetMessage = findViewById(R.id.noInternetMessage);

        retryButton.setOnClickListener(v -> checkNetworkAndUpdateUI());
        checkNetworkAndUpdateUI();
    }

    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = connectivityManager.getActiveNetwork();
            if (network != null) {
                NetworkCapabilities capabilities =
                        connectivityManager.getNetworkCapabilities(network);
                return capabilities != null &&
                        capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                        (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
            }
        } else {
            @SuppressWarnings("deprecation")
            android.net.NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }

        return false;
    }

    private void checkNetworkAndUpdateUI() {
        if (isInternetAvailable()) {
            noInternetMessage.setVisibility(View.INVISIBLE);

            // 복귀할 액티비티로 이동
            if (returnActivityName != null) {
                try {
                    Class<?> returnClass = Class.forName(returnActivityName);
                    Intent intent = new Intent(NetworkCheckActivity.this, returnClass);
                    // 필요하다면 기존 스택 정리
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    // 예외 발생 시 기본적으로 홈으로 이동
                    Intent intent = new Intent(NetworkCheckActivity.this, HomeScreenActivity.class);
                    startActivity(intent);
                }
            } else {
                // 정보가 없으면 홈으로 이동
                Intent intent = new Intent(NetworkCheckActivity.this, HomeScreenActivity.class);
                startActivity(intent);
            }
            finish();
        } else {
            noInternetMessage.setText("인터넷 연결이 여전히 필요합니다.");
            noInternetMessage.setVisibility(View.VISIBLE);
        }
    }
}

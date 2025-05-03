package com.example.weatherapp;

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

    private Button retryButton;
    private TextView noInternetMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_check);

        retryButton = findViewById(R.id.retryButton);
        noInternetMessage = findViewById(R.id.noInternetMessage);  // 메시지 TextView 연결

        // '다시 시도' 버튼 클릭 시 네트워크 상태 확인
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNetworkAndUpdateUI(); // 버튼 클릭 시 네트워크 상태 확인
            }
        });

        // 앱 실행 시 자동으로 네트워크 상태 확인
        checkNetworkAndUpdateUI();
    }

    // 인터넷 연결 상태 확인 메서드
    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Android 6.0 이상에서는 NetworkCapabilities를 사용하여 네트워크 연결 확인
                Network network = connectivityManager.getActiveNetwork();
                if (network != null) {
                    NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
                    if (capabilities != null) {
                        // 네트워크가 인터넷 연결을 지원하는지 확인
                        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
                    }
                }
            } else {
                // Android 6.0 이하에서는 NetworkInfo를 사용
                android.net.NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                return activeNetwork != null && activeNetwork.isConnected();
            }
        }
        return false;
    }

    // 네트워크 상태 확인 후 UI 업데이트
    private void checkNetworkAndUpdateUI() {
        if (isInternetAvailable()) {
            // 인터넷이 연결되었으면 경고 메시지 숨기고 BannerActivity로 이동
            noInternetMessage.setVisibility(View.INVISIBLE);
            // 네트워크가 연결되었으므로 BannerActivity로 이동
            Intent intent = new Intent(NetworkCheckActivity.this, BannerActivity.class);
            startActivity(intent);
            finish(); // 현재 액티비티 종료
        } else {
            // 인터넷이 연결되지 않았다면 경고 메시지 표시
            noInternetMessage.setText("인터넷 연결이 여전히 필요합니다.");
            noInternetMessage.setVisibility(View.VISIBLE);  // 메시지가 보이도록 설정
        }
    }
}

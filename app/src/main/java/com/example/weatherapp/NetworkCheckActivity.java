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

    private TextView noInternetMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_check);

        Button retryButton = findViewById(R.id.retryButton);
        noInternetMessage = findViewById(R.id.noInternetMessage);

        retryButton.setOnClickListener(v -> checkNetworkAndUpdateUI());
        checkNetworkAndUpdateUI();
    }

    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Network network = connectivityManager.getActiveNetwork();
                if (network != null) {
                    NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
                    if (capabilities != null) {
                        // 실제 인터넷 연결이 가능한 네트워크인지 확인
                        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                                && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
                    }
                }
                return false;
            }
        }
        return false;
    }

    private void checkNetworkAndUpdateUI() {
        if (isInternetAvailable()) {
            noInternetMessage.setVisibility(View.INVISIBLE);

            // 네트워크 연결되면 바로 WeatherWeekActivity로 이동 (더미 데이터 X)
            Intent intent = new Intent(NetworkCheckActivity.this, WeatherWeekActivity.class);
            startActivity(intent);
            finish();
        } else {
            noInternetMessage.setText("인터넷 연결이 여전히 필요합니다.");
            noInternetMessage.setVisibility(View.VISIBLE);
        }
    }
}

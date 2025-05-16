package com.example.myapplication_mo;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // activity_main.xml을 화면에 표시
        setContentView(R.layout.activity_main);

        // 버튼 연결
        Button dustCheckButton = findViewById(R.id.dust_check_button);

        dustCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDustPopup();
            }
        });
    }

    // 팝업 다이얼로그 띄우는 메서드
    private void showDustPopup() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dust_popup); // dust_popup.xml 레이아웃 사용
        dialog.setCancelable(true); // 바깥 터치 시 닫을 수 있음
        dialog.show();
    }
}

package com.example.weatherapp;

import android.os.Bundle;
import android.util.Log; // Log 사용을 위해 추가

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// 카카오맵 관련 import 문들
import com.kakao.vectormap.KakaoMap;
import com.kakao.vectormap.KakaoMapReadyCallback;
import com.kakao.vectormap.MapLifeCycleCallback;
import com.kakao.vectormap.MapView;

public class MainActivity extends AppCompatActivity {

    private MapView mapView; // MapView 멤버 변수로 선언
    private KakaoMap mKakaoMap; // KakaoMap 객체를 저장할 멤버 변수 (선택 사항이지만 유용함)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // 기존 EdgeToEdge 활성화 코드
        setContentView(R.layout.activity_main); // activity_main.xml 레이아웃을 화면에 설정

        // --- 카카오맵 초기화 코드 시작 ---
        mapView = findViewById(R.id.map_view); // activity_main.xml에 정의된 MapView의 ID (R.id.map_view)로 변경해야 할 수 있음

        if (mapView != null) {
            mapView.start(new MapLifeCycleCallback() {
                @Override
                public void onMapDestroy() {
                    // 지도 API 가 정상적으로 종료될 때 호출됨
                    // 예: Log.d("KakaoMap", "onMapDestroy");
                }

                @Override
                public void onMapError(Exception error) {
                    // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출됨
                    Log.e("KakaoMap", "onMapError: " + error.getMessage());
                }
            }, new KakaoMapReadyCallback() {
                @Override
                public void onMapReady(KakaoMap kakaoMap) {
                    // 인증 후 API 가 정상적으로 실행될 때 호출됨
                    mKakaoMap = kakaoMap; // KakaoMap 객체를 멤버 변수에 할당
                    Log.d("KakaoMap", "Map is ready!");
                    // 이 시점부터 mKakaoMap 객체를 사용하여 지도 조작 가능
                    // 예: 마커 추가, 카메라 이동, 초기 위치/줌 설정 등
                    // if (mKakaoMap != null) {
                    //    // 초기 위치 설정 예시 (원하는 좌표로 변경)
                    //    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng.from(37.5665, 126.9780), 10);
                    //    mKakaoMap.moveCamera(cameraUpdate);
                    // }
                }

                /*
                // 만약 "지도 초기화 시 다양한 설정"에서 설명한 KakaoMapReadyCallback의 다른 오버라이드 메소드를 사용하려면
                // 여기에 getPosition(), getZoomLevel() 등을 추가하고,
                // mapView.start() 호출 시 new MapLifeCycleCallback() 부분을 빼고 이 KakaoMapReadyCallback만 전달해야 합니다.
                // (공식 문서의 start 메소드 오버로딩 방식 확인 필요)

                @Override
                public com.kakao.vectormap.LatLng getPosition() {
                    return com.kakao.vectormap.LatLng.from(37.406960, 127.115587);
                }

                @Override
                public int getZoomLevel() {
                    return 15;
                }
                */
            });
        } else {
            Log.e("KakaoMap", "MapView (R.id.map_view) is null. Check your activity_main.xml layout file.");
        }
        // --- 카카오맵 초기화 코드 끝 ---

        // 기존 WindowInsets 리스너 코드 (화면 UI 영역 조절)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Activity 생명주기에 맞춰 MapView의 생명주기 호출
    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.resume(); // MapView 다시 시작
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.pause(); // MapView 일시 정지
        }
    }

    // (선택 사항) Activity가 완전히 종료될 때 MapView의 리소스를 해제하려면
    // @Override
    // protected void onDestroy() {
    //     super.onDestroy();
    //     if (mapView != null) {
    //         // 공식 문서에 따라 mapView.finish() 또는 mapView.destroy() 등을 호출할 수 있는지 확인
    //         // MapLifeCycleCallback의 onMapDestroy에서 처리하는 것이 일반적일 수 있음
    //     }
    // }
}
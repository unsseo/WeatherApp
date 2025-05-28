package com.example.basicweatherapp;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import com.kakao.vectormap.KakaoMap;
import com.kakao.vectormap.KakaoMapReadyCallback;
import com.kakao.vectormap.KakaoMapSdk; // KakaoMapSdk import 확인
import com.kakao.vectormap.LatLng;
import com.kakao.vectormap.MapLifeCycleCallback;
import com.kakao.vectormap.MapView;
import com.kakao.vectormap.camera.CameraUpdateFactory;
import com.kakao.vectormap.label.Label;
import com.kakao.vectormap.label.LabelLayer;
import com.kakao.vectormap.label.LabelManager;
import com.kakao.vectormap.label.LabelOptions;
import com.kakao.vectormap.label.LabelStyle;
import com.kakao.vectormap.label.LabelStyles;
import com.kakao.vectormap.label.LabelTextBuilder;
import com.kakao.vectormap.label.LabelTextStyle;

import java.util.ArrayList;
import java.util.List;

// MarkerInfo 클래스는 프로젝트 내에 정의되어 있어야 합니다.
// (이전 답변의 MarkerInfo 예시 참고)

public class WeatherMap extends AppCompatActivity { // 클래스 이름이 WeatherMapActivity로 변경된 것을 반영
    private static final String TAG = "WeatherMapActivity";
    private MapView mapView;
    private KakaoMap mKakaoMap;
    private LabelLayer mLabelLayer;

    private List<MarkerInfo> markerInfoList;
    private LabelStyles markerStyles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ▼▼▼▼▼ KakaoMap SDK 초기화 (직접 호출) ▼▼▼▼▼
        // "7e824790782e5e9289f5169dc0ecb446" 부분을 실제 발급받은 네이티브 앱 키로 반드시 변경하세요.
        // 이 방식은 이 액티비티가 생성될 때마다 KakaoMapSdk.init()을 호출합니다.
        Log.d(TAG, "KakaoMapSdk.init() called in WeatherMapActivity onCreate");
        KakaoMapSdk.init(this, "7e824790782e5e9289f5169dc0ecb446");
        // ▲▲▲▲▲ KakaoMap SDK 초기화 코드 끝 ▲▲▲▲▲

        setContentView(R.layout.weather_map);

        mapView = findViewById(R.id.map_view);

        if (mapView != null) {
            mapView.start(new MapLifeCycleCallback() {
                // ... (기존 MapLifeCycleCallback 내용) ...
                @Override
                public void onMapDestroy() {
                    Log.d(TAG, "onMapDestroy: 지도가 소멸되었습니다.");
                }

                @Override
                public void onMapError(Exception error) {
                    Log.e(TAG, "onMapError: 지도 초기화 중 오류 발생", error);
                }
            }, new KakaoMapReadyCallback() {
                // ... (기존 KakaoMapReadyCallback 내용) ...
                @Override
                public void onMapReady(KakaoMap kakaoMap) {
                    Log.d(TAG, "onMapReady: 지도가 준비되었습니다.");
                    mKakaoMap = kakaoMap;

                    if (mKakaoMap == null) {
                        Log.e(TAG, "onMapReady: KakaoMap 객체가 null입니다.");
                        return;
                    }

                    LabelManager labelManager = mKakaoMap.getLabelManager();
                    if (labelManager == null) {
                        Log.e(TAG, "onMapReady: LabelManager 객체가 null입니다.");
                        return;
                    }
                    mLabelLayer = labelManager.getLayer();
                    if (mLabelLayer == null) {
                        Log.e(TAG, "onMapReady: 기본 LabelLayer 객체가 null입니다.");
                        return;
                    }

                    initializeMarkerData();
                    defineAndRegisterLabelStyles(labelManager);
                    displayMarkers();

                    if (mKakaoMap != null && markerInfoList != null && !markerInfoList.isEmpty()) {
                        MarkerInfo firstMarker = markerInfoList.get(0);
                        try {
                            mKakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(
                                    LatLng.from(firstMarker.getLatitude(), firstMarker.getLongitude()), 14));
                            Log.d(TAG, "Camera moved to first marker.");
                        } catch (Exception e) {
                            Log.e(TAG, "카메라 이동 실패. SDK 버전 또는 API 사용법을 확인하세요.", e);
                        }
                    }
                }

                @Override
                public int getZoomLevel() {
                    return 14;
                }
            });
        } else {
            Log.e(TAG, "onCreate: MapView를 찾을 수 없습니다. weather_map.xml 레이아웃 파일을 확인하세요.");
        }
    }

    // ... (defineAndRegisterLabelStyles, initializeMarkerData, displayMarkers, onResume, onPause, onDestroy 메소드들은 기존과 동일) ...
    private void defineAndRegisterLabelStyles(LabelManager labelManager) {
        // *** R.drawable.red_marker 와 R.drawable.blue_marker는 실제 리소스로 교체 필요 ***
        LabelStyles newStyles = LabelStyles.from("customMarkerStyleId", // 스타일의 고유 ID
                LabelStyle.from(R.drawable.green_marker) // 줌 레벨 1일 때 적용될 스타일
                        .setZoomLevel(1)
                        .setTextStyles(
                                // 줌 레벨 1일 때 첫 번째 줄 텍스트 (예: 온도 "17℃") 스타일
                                LabelTextStyle.from(35, Color.BLACK, 6, Color.WHITE), // 크기 45, 빨간색, 1px 흰색 테두리
                                // 줌 레벨 1일 때 두 번째 줄 텍스트 (예: 도시이름 "서울") 스타일
                                LabelTextStyle.from(25, Color.DKGRAY, 4, Color.WHITE)  // 크기 30, 어두운회색, 1px 흰색 테두리
                        ),
                LabelStyle.from(R.drawable.red_marker) // 줌 레벨 11일 때 적용될 스타일 (현재 텍스트 스타일 없음)
                        .setZoomLevel(11),
                // 만약 줌 레벨 11에서도 텍스트를 다른 크기로 표시하고 싶다면, 여기에도 .setTextStyles(...)를 추가합니다.
                LabelStyle.from(R.drawable.red_marker) // 줌 레벨 15일 때 적용될 스타일
                        .setZoomLevel(15)
                        .setTextStyles(
                                // 줌 레벨 15일 때 첫 번째 줄 텍스트 (예: 온도 "17℃") 스타일
                                LabelTextStyle.from(35, Color.BLACK, 6, Color.GRAY), // 크기 25, 검은색, 1px 회색 테두리
                                // 줌 레벨 15일 때 두 번째 줄 텍스트 (예: 도시이름 "서울") 스타일
                                LabelTextStyle.from(20, Color.GRAY, 4, Color.GRAY)  // 크기 20, 회색, 1px 회색 테두리
                        )
        );
        markerStyles = labelManager.addLabelStyles(newStyles);

        if (markerStyles == null) {
            Log.e(TAG, "Failed to add custom label styles. Basic style fallback.");
            LabelStyle basicStyle = LabelStyle.from();
            markerStyles = labelManager.addLabelStyles(LabelStyles.from(basicStyle));
            if (markerStyles == null) {
                Log.e(TAG, "Failed to add even basic styles.");
            }
        }
    }


    private void initializeMarkerData() {
        markerInfoList = new ArrayList<>();
        // MarkerInfo 생성자 형태는 MarkerInfo.java 파일에 정의된 것을 따라야 합니다.
        // 예: public MarkerInfo(double latitude, double longitude, String... textsToDisplay)
        markerInfoList.add(new MarkerInfo(37.5665, 126.9780, "17℃","서울"));
        markerInfoList.add(new MarkerInfo(37.2636, 127.0286, "21℃","경기"));
        markerInfoList.add(new MarkerInfo(37.8813, 127.7298, "23℃","강원"));
        markerInfoList.add(new MarkerInfo(37.4563, 126.7052, "24℃","인천"));
        markerInfoList.add(new MarkerInfo(36.6425, 127.4897, "20℃","충북"));
        markerInfoList.add(new MarkerInfo(36.5760, 128.5056, "23℃","경북"));
        markerInfoList.add(new MarkerInfo(35.8714, 128.6014, "21℃","대구"));
        markerInfoList.add(new MarkerInfo(36.3504, 127.3845, "22℃","대전"));
        markerInfoList.add(new MarkerInfo(36.4801, 127.2890, "23℃","세종"));
        markerInfoList.add(new MarkerInfo(36.6590, 126.6735, "21℃","충남"));
        markerInfoList.add(new MarkerInfo(35.8242, 127.1480, "19℃","전북"));
        markerInfoList.add(new MarkerInfo(35.5384, 129.3114, "23℃","울산"));
        markerInfoList.add(new MarkerInfo(35.1800, 129.0756, "18℃","부산"));
        markerInfoList.add(new MarkerInfo(35.2383, 128.6924, "23℃","경남"));
        markerInfoList.add(new MarkerInfo(35.1601, 126.8515, "23℃","광주"));
        markerInfoList.add(new MarkerInfo(34.8163, 126.4629, "21℃","전남"));
        markerInfoList.add(new MarkerInfo(33.4996, 126.5312, "25℃","제주"));
    }

    private void displayMarkers() {
        if (mLabelLayer == null || markerInfoList == null || markerInfoList.isEmpty() || markerStyles == null) {
            Log.w(TAG, "Cannot display markers: Dependencies not ready.");
            return;
        }
        mLabelLayer.removeAll(); // 기존 라벨 제거

        for (MarkerInfo marker : markerInfoList) {
            LatLng position = LatLng.from(marker.getLatitude(), marker.getLongitude());
            String[] textsFromMarkerInfo = marker.getTextsToDisplay(); // MarkerInfo에 정의된 메소드

            LabelOptions options = LabelOptions.from(position)
                    .setStyles(markerStyles);

            if (textsFromMarkerInfo != null && textsFromMarkerInfo.length > 0) {
                try {
                    LabelTextBuilder textBuilder = new LabelTextBuilder();
                    textBuilder.setTexts(textsFromMarkerInfo);
                    options.setTexts(textBuilder);
                } catch (NoSuchMethodError e) {
                    Log.e(TAG, "Error using LabelTextBuilder.setTexts(String[]). Check API for your SDK version. Falling back if possible.", e);
                    // 이전에 논의된 대체 방법 (LabelOptions.setTexts(String...) 직접 사용 등)을 여기에 구현할 수 있습니다.
                    // 예를 들어, textsFromMarkerInfo의 길이에 따라 options.setTexts(textsFromMarkerInfo[0], textsFromMarkerInfo[1]); 등
                } catch (NoClassDefFoundError e) {
                    Log.e(TAG, "LabelTextBuilder class not found. Check SDK dependencies and version.", e);
                } catch (Exception e) {
                    Log.e(TAG, "Error while trying to set texts for label.", e);
                }
            }
            mLabelLayer.addLabel(options);
        }

        // VVVVVV 이 부분이 수정되었습니다 VVVVVV
        Label[] allLabels = mLabelLayer.getAllLabels(); // 타입을 Label[]로 변경
        if (allLabels != null) {
            Log.i(TAG, "Displayed " + allLabels.length + " labels on the map."); // .size() 대신 .length 사용
        } else {
            Log.i(TAG, "Displayed 0 labels on the map (label array is null).");
        }
        // ^^^^^^ 수정 완료 ^^^^^^
    }

    @Override
    protected void onResume() {
        super.onResume();
        // KakaoMapSdk.init()이 여기서 호출되지 않도록 주의
        if (mapView != null) {
            // mapView.start() 이후에 MapView 객체가 유효할 때만 resume() 호출
            // init() 실패 시 mapView 내부 객체가 null일 수 있어 NPE 발생 가능 (이전 Logcat 참고)
            try {
                mapView.resume();
            } catch (Exception e) {
                Log.e(TAG, "Error resuming MapView, SDK might not be initialized or map failed to start.", e);
            }
        }
        Log.d(TAG, "onResume: 액티비티가 화면에 다시 나타났습니다.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        // KakaoMapSdk.init()이 여기서 호출되지 않도록 주의
        if (mapView != null) {
            try {
                mapView.pause();
            } catch (Exception e) {
                Log.e(TAG, "Error pausing MapView.", e);
            }
        }
        Log.d(TAG, "onPause: 액티비티가 화면에서 가려졌습니다.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: 액티비티가 소멸되었습니다.");
        mKakaoMap = null;
        mLabelLayer = null;
        if (markerInfoList != null) markerInfoList.clear();
        // mapView = null; // MapView 자체를 null로 만들 필요는 보통 없습니다.
        // MapLifeCycleCallback의 onMapDestroy에서 내부 리소스가 해제됩니다.
    }
}

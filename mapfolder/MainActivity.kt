package com.example.map_project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import net.daum.mf.map.api.MapView // ViewBinding 사용 시 직접 import 안 해도 될 수 있음
import com.example.map_project.databinding.ActivityMainBinding // ViewBinding import
import net.daum.mf.map.api.MapPoint // 예시로 중심점 설정을 위해 import

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding // ViewBinding 사용

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // XML에 추가한 MapView 객체를 가져옵니다.
        // MyApplication에서 KakaoSdk.init()이 정상적으로 호출되어야 MapView가 동작합니다.
        val mapView = binding.mapView // ViewBinding으로 접근

        // (선택 사항) 지도 초기 중심점 및 줌 레벨 설정 예시
        // 실제 앱에서는 MapViewEventListener의 onMapViewInitialized 콜백에서 설정하는 것이 더 좋습니다.
        // mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.53737528, 127.00557633), true) // 서울 예시
        // mapView.setZoomLevel(4, true)
    }
}

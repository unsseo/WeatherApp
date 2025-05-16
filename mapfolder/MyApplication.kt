package com.example.map_project

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
// import com.kakao.sdk.common.util.Utility // 디버그 키 해시 확인용 (필요시 사용)
// import android.util.Log // 디버그 키 해시 확인용 (필요시 사용)

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // 카카오 SDK 초기화 (네이티브 앱 키 사용)
        KakaoSdk.init(this, "7e824790782e5e9289f5169dc0ecb446")

        // 디버그 키 해시 확인 (개발 중에만 사용하고, 확인 후에는 주석 처리하거나 삭제)
        // val keyHash = Utility.getKeyHash(this)
        // Log.d("MyApplication", "Key Hash: $keyHash")
    }
}

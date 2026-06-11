package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivitySplashBinding

/**
 * SplashActivity — 앱을 켜면 가장 먼저 보이는 시작 화면(스플래시).
 *
 * 도감 카드 한 장이 떠 있는 디자인을 잠깐 보여준 뒤, 2.5초 후 자동으로 홈으로 넘어간다.
 * (AndroidManifest 에서 이 액티비티가 LAUNCHER 로 지정되어 앱의 진입점이 된다)
 */
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handler.postDelayed: "지정한 시간(2500ms = 2.5초) 뒤에 이 코드를 실행하라"는 예약.
        // Looper.getMainLooper() → 화면(UI)을 다루는 메인 스레드에서 실행되도록 지정.
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))   // 홈으로 이동
            finish()    // 스플래시를 닫아, 뒤로가기로 다시 돌아오지 않게 함
        }, 2500)
    }
}
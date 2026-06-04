package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivitySplashBinding

// 앱 실행 시 약 2.5초간 표시된 뒤 자동으로 메인 화면으로 넘어가는 시작 화면
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 뷰 바인딩으로 레이아웃 연결 (6장에서 배운 방식)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handler + postDelayed: 2500밀리초(2.5초) 뒤에 안의 코드를 실행
        Handler(Looper.getMainLooper()).postDelayed({
            // 메인 화면으로 이동
            startActivity(Intent(this, MainActivity::class.java))
            // 스플래시는 뒤로가기로 다시 오면 안 되므로 화면을 종료
            finish()
        }, 2500)
    }
}
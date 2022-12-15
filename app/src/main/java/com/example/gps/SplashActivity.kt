package com.example.gps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.gps.user.IntroActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            //Intent 생성
            val intent = Intent(this@SplashActivity, IntroActivity::class.java)
            //Intent 실행
            startActivity(intent)
        }, 3000)



    }
}
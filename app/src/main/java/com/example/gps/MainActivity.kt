package com.example.gps

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.gps.fragment.*
import com.example.gps.location.LocationActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            val information =
                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
            val signatures = information.signingInfo.apkContentsSigners
            val md = MessageDigest.getInstance("SHA")
            for (signature in signatures) {
                val md: MessageDigest
                md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                var hashcode = String(Base64.encode(md.digest(), 0))
                Log.d("hashcode", "" + hashcode)
            }
        } catch (e: Exception) {
            Log.d("hashcode", "에러::" + e.toString())

        }

        val bnv = findViewById<BottomNavigationView>(R.id.bnv)
        val fl = findViewById<FrameLayout>(R.id.fl)
        val imgLogout = findViewById<ImageView>(R.id.imgLogout)
        val tvMap = findViewById<TextView>(R.id.tvMap)

        tvMap.setOnClickListener {
            val intent = Intent(this@MainActivity, MapActivity::class.java)
//            val intent = Intent(this@MainActivity, LocationActivity::class.java)
            startActivity(intent)
        }

        supportFragmentManager.beginTransaction().replace(
            R.id.fl,
            HomeFragment()
        ).commit()

        bnv.setOnItemSelectedListener { item ->
            // item -> 내가 선택한 item의 정보
            Log.d("id", item.itemId.toString())
            when (item.itemId) {
                R.id.tap1 -> {
                    // Fragment1Home 부분화면으로 갈아끼워준다
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fl,
                        RankFragment()
                    ).commit()
                }
                R.id.tap2 -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fl,
                        AdviseFragment()
                    ).commit()
                }
                R.id.tap3 -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fl,
                        HomeFragment()
                    ).commit()
                }
                R.id.tap4 -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fl,
                        OotdFragment()
                    ).commit()
                }
                R.id.tap5 -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fl,
                        ClosetFragment()
                    ).commit()
                }
            }
            true
        }


    }

    // 텍스트 입력 박스 이외 다른 영역 클릭시 키보드 내리기
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val focusView = currentFocus
        if (focusView != null && ev != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev.x.toInt()
            val y = ev.y.toInt()

            if (!rect.contains(x, y)) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm?.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }





}
package com.example.gps

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gps.fragment.*
import com.example.gps.weather.WeatherVO
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //프로젝트 키 해시 가져오는 코드
//        try {
//            val information =
//                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
//            val signatures = information.signingInfo.apkContentsSigners
//            val md = MessageDigest.getInstance("SHA")
//            for (signature in signatures) {
//                val md: MessageDigest
//                md = MessageDigest.getInstance("SHA")
//                md.update(signature.toByteArray())
//                var hashcode = String(Base64.encode(md.digest(), 0))
//                Log.d("hashcode", "" + hashcode)
//            }
//        } catch (e: Exception) {
//            Log.d("hashcode", "에러::" + e.toString())
//        }

        val bnv = findViewById<BottomNavigationView>(R.id.bnv)
        val fl = findViewById<FrameLayout>(R.id.fl)
        val imgLogout = findViewById<ImageView>(R.id.imgLogout)
        val tvMap = findViewById<TextView>(R.id.tvMap)

        val weatherList = ArrayList<WeatherVO>()

        tvMap.setOnClickListener {
            val intent = Intent(this@MainActivity, MapActivity::class.java)
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


}
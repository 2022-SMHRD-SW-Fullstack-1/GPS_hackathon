package com.example.gps

import android.content.Intent
import android.os.Build

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView

import android.widget.TextView
import androidx.annotation.RequiresApi
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gps.fragment.*
import com.example.gps.weather.WeatherVO
import org.json.JSONObject

import com.example.gps.chat.ChatActivity
import com.example.gps.fragment.*
import com.example.gps.user.IntroActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private var HomeFragment: HomeFragment? = null

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bnv = findViewById<BottomNavigationView>(R.id.bnvChat)
        val fl = findViewById<FrameLayout>(R.id.fl)
        val imgLogout = findViewById<ImageView>(R.id.imgLogout)
        val img_Chat = findViewById<ImageView>(R.id.img_Chat)
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

        img_Chat.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
        }



        imgLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
            finish()
        }


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


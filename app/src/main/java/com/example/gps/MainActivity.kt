package com.example.gps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.gps.chat.ChatActivity
import com.example.gps.fragment.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bnv = findViewById<BottomNavigationView>(R.id.bnvChat)
        val fl = findViewById<FrameLayout>(R.id.fl)
        val imgLogout = findViewById<ImageView>(R.id.imgLogout)
        val img_Chat = findViewById<ImageView>(R.id.img_Chat)


        supportFragmentManager.beginTransaction().replace(
            R.id.fl,
            RankFragment()
        ).commit()

        img_Chat.setOnClickListener{
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
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
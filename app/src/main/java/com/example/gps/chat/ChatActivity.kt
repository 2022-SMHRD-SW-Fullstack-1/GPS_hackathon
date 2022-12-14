package com.example.gps.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.gps.MainActivity
import com.example.gps.R
import com.example.gps.chat.fagment.ChatFragment2
import com.example.gps.chat.fagment.ChatFragment1
import com.google.android.material.bottomnavigation.BottomNavigationView

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val bnvChat = findViewById<BottomNavigationView>(R.id.bnvChat)
        val fl = findViewById<FrameLayout>(R.id.fl)

        supportFragmentManager.beginTransaction().replace(
            R.id.fl,
            ChatFragment1()
        ).commit()



        bnvChat.setOnItemSelectedListener { item ->
            // item -> 내가 선택한 item의 정보
            Log.d("id", item.itemId.toString())
            when (item.itemId) {
                R.id.chat_home -> {
                    // Fragment1Home 부분화면으로 갈아끼워준다
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fl,
                        ChatFragment1()
                    ).commit()
                }
                R.id.chat_list -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fl,
                        ChatFragment2()
                    ).commit()
                }
            }
            true
        }


    }
}

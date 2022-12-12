package com.example.gps.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.gps.MainActivity
import com.example.gps.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        lateinit var auth: FirebaseAuth

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        auth = Firebase.auth

        val etLoginEmail = findViewById<EditText>(R.id.etLoginEmail)
        val etLoginPw = findViewById<EditText>(R.id.etLoginPw)
        val btnUserLogin = findViewById<Button>(R.id.btnUserLogin)
        val btnUserJoin = findViewById<Button>(R.id.btnUserJoin)
        val tvUserSearch = findViewById<TextView>(R.id.tvUserSearch)

        btnUserJoin.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }

        tvUserSearch.setOnClickListener {

        }


        btnUserLogin.setOnClickListener {
            val email = etLoginEmail.text.toString()
            val pw = etLoginPw.text.toString()

            if (email.length == 0 || pw.length == 0) {
                Toast.makeText(this, "이메일 혹은 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show()

            } else {
                auth.signInWithEmailAndPassword(email, pw)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()

                            // 로그인을 성공했으면 MainActivity로 이동
                            val intent = Intent(this@IntroActivity, UserActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}
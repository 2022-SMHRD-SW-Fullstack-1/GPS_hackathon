package com.example.gps.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.gps.MainActivity
import com.example.gps.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class IntroActivity : AppCompatActivity() {

    //인증기능 사용
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        auth = Firebase.auth

        val btnIntroLogin = findViewById<Button>(R.id.btnIntroLogin)
        val btnIntroJoin = findViewById<Button>(R.id.btnIntroJoin)
        val btnIntroNo = findViewById<Button>(R.id.btnIntroNo)

        //btnIntroLogin 누르면
        btnIntroLogin.setOnClickListener {
            //LoginActivity로 이동

            //Intent생성
            val intent = Intent(this@IntroActivity, LoginActivity::class.java)

            //Intent 실행
            startActivity(intent)

        }

        //btnIntroJoin 누르면
        btnIntroJoin.setOnClickListener {
            //JoinActivity로 이동

            val intent = Intent(this@IntroActivity, JoinActivity::class.java)

            startActivity(intent)
        }

        //btnIntroNo 누르면 Firebase에서 익명으로 로그인 할 수 있는 권한 받아오기
        btnIntroNo.setOnClickListener {
            //비회원로그인(익명로그인)
            auth.signInAnonymously().addOnCompleteListener(this){task ->
                if (task.isSuccessful){
                    //익명 로그인 성공
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@IntroActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                }else{
                    //익명 로그인 실패
                        //서버 문제
                    Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }
}
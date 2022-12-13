package com.example.gps.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView.FindListener
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.gps.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ConfirmActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)

        val tvConfirmEmail = findViewById<TextView>(R.id.tvConfirmEmail)
        val etConfirmPw = findViewById<EditText>(R.id.etConfirmPw)
        val btnConfirmOk = findViewById<Button>(R.id.btnConfirmOk)
        val btnConfirmCancel = findViewById<Button>(R.id.btnConfirmCancel)

        auth = Firebase.auth

        val user = Firebase.auth.currentUser

        tvConfirmEmail.text = user?.email

        btnConfirmOk.setOnClickListener {
            val email = user!!.email
            val pwCk = etConfirmPw.text.toString()

            if (email!!.length == 0 || pwCk.length == 0) {
                Toast.makeText(this, "이메일 혹은 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show()

            } else {
                auth.signInWithEmailAndPassword(email!!, pwCk)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()

                            // 로그인을 성공했으면 MainActivity로 이동
                            val intent = Intent(this, UserActivity::class.java)
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
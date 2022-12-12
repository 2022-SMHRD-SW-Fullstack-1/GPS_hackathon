package com.example.gps.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView.FindListener
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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
            val pwCk = etConfirmPw.text.toString()


        }

    }
}
package com.example.gps.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.gps.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserActivity : AppCompatActivity() {

    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        val tvInfoEmail = findViewById<TextView>(R.id.tvInfoEmail)
        val etInfoPw = findViewById<EditText>(R.id.etInfoPw)
        val etInfoPwCk = findViewById<EditText>(R.id.etInfoPwCk)
        val etInfoNick = findViewById<EditText>(R.id.etInfoNick)
        val ivInfoProfile = findViewById<ImageView>(R.id.ivInfoProfile)
        val btnInfoOk = findViewById<Button>(R.id.btnInfoOk)
        val btnInfoDelAcc = findViewById<Button>(R.id.btnInfoDelAcc)

        auth = Firebase.auth
        val database = Firebase.database
        val infoRef = database.getReference("users")

        val user = Firebase.auth.currentUser

        tvInfoEmail.text = user?.email
        val uid = user?.uid

        btnInfoOk.setOnClickListener {

        }
    }
}
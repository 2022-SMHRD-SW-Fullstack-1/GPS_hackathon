package com.example.gps.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

import com.example.gps.R
import com.example.gps.utils.FBAuth
import com.example.gps.utils.FBdatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class UserActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var infoRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        val tvInfoEmail = findViewById<TextView>(R.id.tvInfoEmail)
        val etInfoPw = findViewById<EditText>(R.id.etInfoPw)
        val etInfoPwCk = findViewById<EditText>(R.id.etInfoPwCk)
        val btnChangeInfo = findViewById<Button>(R.id.btnChangeInfo)
        val btnInfoDelAcc = findViewById<TextView>(R.id.btnInfoDelAcc)
        val imgUserBack = findViewById<ImageView>(R.id.imgUserBack)

        auth = Firebase.auth

        infoRef = FBdatabase.getUserRef()

        tvInfoEmail.text = FBAuth.getCurrentUser()?.email

        imgUserBack.setOnClickListener {
            onBackPressed()
        }

        btnChangeInfo.setOnClickListener {
            val newPw = etInfoPw.text.toString()
            val newPwCk = etInfoPwCk.text.toString()

            // 패스워드 수정
            if (newPw.isNotEmpty()) {
                if (newPw == newPwCk && newPw.length >= 8) {
                    changePassword(newPw)
                } else {
                    Toast.makeText(this, "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
                }
            }
            onBackPressed()
        }

        btnInfoDelAcc.setOnClickListener {
            deleteAccount()
        }

    }


    fun changePassword(password: String) {
        FBAuth.getCurrentUser()!!.updatePassword(password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "비밀번호 변경", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "비밀번호 변경 실패", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun deleteAccount() {

        // Firebase에서 데이터를 받아오는 Listener
        val postListener = (object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (model in snapshot.children) {
                    val key = model.key
                    if (FBAuth.getUid() == key) {
                        if (key != null) {
                            infoRef.child(key).removeValue()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        infoRef.addValueEventListener(postListener)

        FBAuth.getCurrentUser()!!.delete().addOnCompleteListener { task ->
            if (task.isSuccessful) {

                Toast.makeText(this, "회원탈퇴가 완료되었습니다", Toast.LENGTH_LONG).show()

                //로그아웃처리
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, IntroActivity::class.java)
                startActivity(intent)
                finish()


            } else {
                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }
}
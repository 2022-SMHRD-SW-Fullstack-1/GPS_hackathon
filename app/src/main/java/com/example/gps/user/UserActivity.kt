package com.example.gps.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.fullstackapplication.utils.FBAuth
import com.example.fullstackapplication.utils.FBdatabase
import com.example.gps.R
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
        val btnInfoDelAcc = findViewById<Button>(R.id.btnInfoDelAcc)

        auth = Firebase.auth

        infoRef = FBdatabase.getUserRef()

        tvInfoEmail.text = FBAuth.getCurrentUser()?.email

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
                    val item = model.getValue<JoinVO>()
                    val itemKey = model.key
                    if (FBAuth.getUid() == item?.uid) {
                        if (itemKey != null) {
                            infoRef.child(itemKey).removeValue()
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

                Toast.makeText(this, "아이디 삭제가 완료되었습니다", Toast.LENGTH_LONG).show()

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
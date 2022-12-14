package com.example.gps.user

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.*
import com.example.gps.R
import com.example.gps.utils.FBAuth
import com.example.gps.utils.FBdatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class JoinActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var profileStorage: FirebaseStorage
    var email: String = ""
    var isJoin = true
    var infoRef = FBdatabase.getUserRef()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        val etJoinEmail = findViewById<EditText>(R.id.etJoinEmail)
        val etJoinPw = findViewById<EditText>(R.id.etJoinPw)
        val etJoinPwCk = findViewById<EditText>(R.id.etJoinPwCk)
        val etJoinNick = findViewById<EditText>(R.id.etJoinNick)
        val btnJoin = findViewById<Button>(R.id.btnJoin)
        val imgBack = findViewById<ImageView>(R.id.imgBack)
//        val svJoinCk = findViewById<ScrollView>(R.id.svJoinCk)
//        val cbJoinCk = findViewById<CheckBox>(R.id.cbJoinCk)

        profileStorage = FirebaseStorage.getInstance();
        auth = Firebase.auth
        val userRef = FBdatabase.getUserRef()

        imgBack.setOnClickListener {
            onBackPressed()
        }

        btnJoin.setOnClickListener {
            email = etJoinEmail.text.toString()
            val pw = etJoinPw.text.toString()
            val checkPw = etJoinPwCk.text.toString()
            val nick = etJoinNick.text.toString()

            // EditText??? ????????? ?????????

            if (email.isEmpty()) {
                isJoin = false
                Toast.makeText(this, "???????????? ??????????????????", Toast.LENGTH_SHORT).show()
            }
            if (pw.isEmpty()) {
                isJoin = false
                Toast.makeText(this, "??????????????? ??????????????????", Toast.LENGTH_SHORT).show()
            }
            if (checkPw.isEmpty()) {
                isJoin = false
                Toast.makeText(this, "???????????? ???????????? ??????????????????", Toast.LENGTH_SHORT).show()
            }
            if (nick.isEmpty()) {
                isJoin = false
                Toast.makeText(this, "???????????? ??????????????????", Toast.LENGTH_SHORT).show()
            }

            // ???????????? == ????????? ??????????????????
            if (pw != checkPw) {
                isJoin = false
                Toast.makeText(this, "??????????????? ?????? ??????????????????", Toast.LENGTH_SHORT).show()
            }

            // ??????????????? 8?????? ??????
            if (pw.length < 8) {
                isJoin = false
                Toast.makeText(this, "??????????????? 8?????? ?????? ??????????????????", Toast.LENGTH_SHORT).show()
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                isJoin = false
                Toast.makeText(this, "????????? ????????? ????????????", Toast.LENGTH_SHORT).show()
            }

//            if (!cbJoinCk.isChecked) {
//                isJoin = false
//                Toast.makeText(this, "????????? ??????????????????", Toast.LENGTH_SHORT).show()
//            }

            if (isJoin) {
                auth.createUserWithEmailAndPassword(email, pw)
                    // ???????????? ????????? ??????
                    // -> create??? ????????? ?????? ???????????? 2???(email, pw)???
                    // ????????? ???????????? ?????? ??????(Firebase??? ??????)

                    .addOnCompleteListener(this) { task ->
                        // ???????????? ?????? ????????? ????????? ???????????? ?????????
                        // task : ?????? ??? ???????????? ?????? ??????(?????? or ??????)
                        if (task.isSuccessful) {
                            // ???????????? ??? ?????? ?????? ??????
                            val uid = FBAuth.getUid()
                            var key = FBdatabase.getUserRef().push().key.toString()//uid?????? ?????? ????????????

                            val userInfo = JoinVO(uid, nick, "defaultuserImage", email, key)

                            userRef.child(uid).setValue(userInfo)

                            Toast.makeText(this, "???????????? ??????!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@JoinActivity, IntroActivity::class.java)
                            startActivity(intent)

                            finish()
                        } else {
                            try{
                                task.getResult();
                            }catch (e: Exception) {
                                Toast.makeText(this, "????????? ??????????????????", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
            }

            val sharedPreferences = getSharedPreferences("saveLogin", Context.MODE_PRIVATE)

            var editor = sharedPreferences.edit()
            editor.putString("saveEmail", "")
            editor.putBoolean("saveCk", false)

            editor.commit()
        }
    }
}
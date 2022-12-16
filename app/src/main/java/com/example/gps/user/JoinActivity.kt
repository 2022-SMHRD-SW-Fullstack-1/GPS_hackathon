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

            // EditText에 내용이 있는지

            if (email.isEmpty()) {
                isJoin = false
                Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
            if (pw.isEmpty()) {
                isJoin = false
                Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
            if (checkPw.isEmpty()) {
                isJoin = false
                Toast.makeText(this, "비밀번호 재입력을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
            if (nick.isEmpty()) {
                isJoin = false
                Toast.makeText(this, "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show()
            }

            // 비밀번호 == 재입력 비밀번호인지
            if (pw != checkPw) {
                isJoin = false
                Toast.makeText(this, "비밀번호를 같게 입력해주세요", Toast.LENGTH_SHORT).show()
            }

            // 비밀번호는 8자리 이상
            if (pw.length < 8) {
                isJoin = false
                Toast.makeText(this, "비밀번호를 8자리 이상 입력해주세요", Toast.LENGTH_SHORT).show()
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                isJoin = false
                Toast.makeText(this, "이메일 형식이 아닙니다", Toast.LENGTH_SHORT).show()
            }

//            if (!cbJoinCk.isChecked) {
//                isJoin = false
//                Toast.makeText(this, "약관에 동의해주세요", Toast.LENGTH_SHORT).show()
//            }

            if (isJoin) {
                auth.createUserWithEmailAndPassword(email, pw)
                    // 사용자를 만드는 작업
                    // -> create가 보내고 있는 전달인자 2개(email, pw)는
                    // 실제로 화원가입 정보 전달(Firebase로 전달)

                    .addOnCompleteListener(this) { task ->
                        // 로그인이 성공 했는지 아닌지 확인하는 리스너
                        // task : 보낸 후 결과값이 담겨 있음(성공 or 실패)
                        if (task.isSuccessful) {
                            // 성공했을 때 실행 시킬 코드
                            val uid = FBAuth.getUid()
                            var key = FBdatabase.getUserRef().push().key.toString()//uid값을 먼저 만들어줌

                            val userInfo = JoinVO(uid, nick, "임의의값", email, key)

                            userRef.child(uid).setValue(userInfo)

                            Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@JoinActivity, IntroActivity::class.java)
                            startActivity(intent)

                            finish()
                        } else {
                            try{
                                task.getResult();
                            }catch (e: Exception) {
                                Toast.makeText(this, "중복된 이메일입니다", Toast.LENGTH_LONG).show();
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
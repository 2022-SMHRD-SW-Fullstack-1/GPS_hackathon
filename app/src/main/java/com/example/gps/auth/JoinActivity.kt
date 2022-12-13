package com.example.gps.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.gps.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class JoinActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        val btnJoinJoin =  findViewById<Button>(R.id.btnJoinJoin)
        val etJoinEmail = findViewById<EditText>(R.id.etJoinEmail)
        val etJoinPw =  findViewById<EditText>(R.id.etJoinPw)
        val etJoinCheck =  findViewById<EditText>(R.id.etJoinCheck)

        //auth초기화
        auth = Firebase.auth
        //Firebase.auth: 로그인, 회원가입, 인증 시스템에 대한 모든 기능이 담겨있음


        //btnJoinJoin 눌렀을 때
        btnJoinJoin.setOnClickListener {
            var isJoin = true//조건을 만족 했는지 안했는지

            val email = etJoinEmail.text.toString()
            val pw = etJoinPw.text.toString()
            val pwCheck = etJoinCheck.text.toString()

            //EditText의 내용이 있는지 확인
            if (email.isEmpty()){
                isJoin = false
                Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }

            if (pw.isEmpty()){
                isJoin = false
                Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }

            if (pwCheck.isEmpty()){
                isJoin = false
                Toast.makeText(this, "비밀번호 확인을 입력해주세요", Toast.LENGTH_SHORT).show()
            }

            //비밀번호랑 비밀번호 확인이 일치하는지
            if(pw != pwCheck){
                isJoin = false
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            }

            //비밀번호가 8자리 이상인지
            if (pw.length<8){
                isJoin = false
                Toast.makeText(this, "비밀번호를 8자리 이상 입력해주세요", Toast.LENGTH_SHORT).show()
            }
            //Firebase에 규칙은 정해져 있지만 사용자는 그 규칙을 모르기 때문에 Toast를 통해서 규칙을 알려줘야 함


            if(isJoin){
                //회원가입 진행
                //auth 기능을 활용하여 회원가입 하기: createUserWithEmailAndPassword
                //create가 보내고 있는 전달 인자 2개(email, pw)는 실제로 회원가입 정보를 전달(Firebase)
                auth.createUserWithEmailAndPassword(email, pw)
                    .addOnCompleteListener(this) { task ->
                        //task: 성공, 실패 여부를 가지고 있음
                        //task가 보낸 결과
                        if (task.isSuccessful) {
                            //성공했을 때 실행시킬 코드
                            Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@JoinActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()//뒤로 가기 눌렀을 때 다시 회원가입 페이지로 오지 않게!
                        } else {
                            //실패했을 때 실행시킬 코드
                            Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()

                        }
                    }
            }


//            if(pw == pwCheck){
//                Toast.makeText(this, "$email, $pw", Toast.LENGTH_SHORT).show()
//            }else{
//                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
//            }


        }



    }
}
package com.example.fullstackapplication.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class FBAuth {

    companion object{

        lateinit var auth : FirebaseAuth

        fun getUid() : String{
            auth = FirebaseAuth.getInstance()
            return auth.currentUser!!.uid
        }

        fun getCurrentUser() : FirebaseUser? {
            auth = FirebaseAuth.getInstance()
            return auth.currentUser
        }

        //현재 시간을 가져오는 함수
        fun getTime() : String {
            //calendar 객체를 activity에서 사용하려면 getInstance()를 활용해야 한다.
            val currentTime = Calendar.getInstance().time

            //시간 형식, 시간 지역 설정 ex) yyyy-mm-dd HH:mm:ss
            val time = SimpleDateFormat("yyyy.MM.dd HH:mm:ss",
                Locale.KOREA).format(currentTime)
            return time
        }

    }
}
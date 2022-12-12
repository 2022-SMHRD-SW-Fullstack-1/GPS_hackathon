package com.example.gps.utils

import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class FBAuth {
    companion object {
        lateinit var auth: FirebaseAuth

        fun getUid(): String {

            auth = FirebaseAuth.getInstance()

            //리턴 타입 String으로 지정해놨기 때문에 toString 안해줘도 됨
            return auth.currentUser!!.uid
        }

        //현재 시간을 가져오는 함수
        fun getTime(): String {
            //Calendar 객체는 getInstance() 메서드로 객체를 생성해야 함
            val currentTime = Calendar.getInstance().time
            //시간을 나타낼 형식, 어느 나라의 시간을 가져올 건지 설정
            //년월 => "yyyy.MM.dd"
            //시간 => "HH:mm:ss"
            val time = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA).format(currentTime)
//            val time = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA).format(currentTime)

            return time
        }


    }
}
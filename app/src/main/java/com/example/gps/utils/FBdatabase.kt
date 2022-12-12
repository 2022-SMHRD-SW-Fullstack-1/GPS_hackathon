package com.example.gps.utils

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBdatabase {

    //RealTime Database 사용은 이 클래스를 통해서 진행
    companion object{
        val db = Firebase.database

        //FBdatabase.getBoardRef()
        fun getBoardRef(): DatabaseReference {
            return db.getReference("board")
        }

        fun getContentRef(): DatabaseReference {
            return db.getReference("content")
        }

        fun getBookMarkRef(): DatabaseReference {
            return db.getReference("bookmarklist")
        }
        //database 인스턴스를 클래스마다 생성할 필요 없음 필요한 거 여기에 만들어서 호출만 하면 됨됨

    }

}
package com.example.fullstackapplication.utils

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBdatabase {

    //reattime database사용은 이 클래스 통해서 진행
    //FBdatabse.getBookmarkRef()
    companion object{
        val database = Firebase.database

        fun getUserRef() : DatabaseReference{
            return database.getReference("users")
        }

        fun getBoardRef() : DatabaseReference{
            return database.getReference("board")
        }
        fun  getContentRef() : DatabaseReference{
            return database.getReference("content")
        }

        fun getBookmarkRef() :DatabaseReference{
            return database.getReference("bookmarklist")
        }
    }
}
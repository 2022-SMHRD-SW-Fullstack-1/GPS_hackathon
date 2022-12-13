package com.example.gps.utils

import android.util.Log
import com.example.fullstackapplication.utils.FBAuth
import com.example.fullstackapplication.utils.FBdatabase
import com.example.gps.user.JoinVO
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class Nickname {

    companion object {

        private val infoRef = FBdatabase.getUserRef()

        fun getNickname() : String{
            var nickname = ""

            val postListener = (object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (model in snapshot.children) {
                        val item = model.getValue<JoinVO>()
                        Log.d("함수안item", item.toString())
                        if (FBAuth.getUid() == item?.uid) {
                            nickname = item.nick
                            Log.d("함수안nickname", nickname)
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
            infoRef.addValueEventListener(postListener)

            return nickname
        }
    }
}
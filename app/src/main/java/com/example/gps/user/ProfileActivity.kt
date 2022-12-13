package com.example.gps.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.fullstackapplication.utils.FBAuth
import com.example.fullstackapplication.utils.FBdatabase
import com.example.gps.R
import com.example.gps.utils.Nickname
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import de.hdodenhof.circleimageview.CircleImageView

class ProfileActivity : AppCompatActivity() {

    lateinit var infoRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val etInfoNick = findViewById<EditText>(R.id.etInfoNick)
        val imgChangeProfile = findViewById<ImageView>(R.id.imgChangeProfile)
        val civSelectProfile = findViewById<CircleImageView>(R.id.civSelectProfile)

        infoRef = FBdatabase.getUserRef()

        val currentNick = intent.getStringExtra("nick")
        Log.d("프로필닉네임", currentNick.toString())

        etInfoNick.setText(currentNick)

        imgChangeProfile.setOnClickListener {

            val newNick = etInfoNick.text.toString()

            // 닉네임 수정
            if (newNick.isNotEmpty()) {
                // Firebase에서 데이터를 받아오는 Listener
                val postListener = (object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (model in snapshot.children) {
                            val item = model.getValue<JoinVO>()
                            val itemKey = model.key
                            if (FBAuth.getUid() == item?.uid) {
                                if (itemKey != null) {
                                    infoRef.child(itemKey).child("nick").setValue(newNick)
                                    Toast.makeText(
                                        this@ProfileActivity,
                                        "닉네임이 변경되었습니다 $newNick",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
                infoRef.addValueEventListener(postListener)
            }
        }
    }
}
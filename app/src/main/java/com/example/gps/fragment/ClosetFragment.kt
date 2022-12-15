package com.example.gps.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.fullstackapplication.utils.FBAuth
import com.example.fullstackapplication.utils.FBdatabase
import com.example.gps.R
import com.example.gps.user.JoinVO
import com.example.gps.user.ProfileActivity
import com.example.gps.user.UserActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import de.hdodenhof.circleimageview.CircleImageView

class ClosetFragment : Fragment() {
    override fun onResume() {
        super.onResume()
        changeInfo()
    }

    lateinit var infoRef: DatabaseReference
    lateinit var civProfile: CircleImageView
    lateinit var tvClosetNick: TextView
    var nickname: String = ""
    var imgUrl: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_closet, container, false)

        tvClosetNick = view.findViewById<TextView>(R.id.tvClosetNick)
        val btnChangeProfile = view.findViewById<Button>(R.id.btnChangeProfile)
        val btnInfoChange = view.findViewById<Button>(R.id.btnInfoChange)
        civProfile = view.findViewById(R.id.civProfile)

        infoRef = FBdatabase.getUserRef()

        btnInfoChange.setOnClickListener {
            val intent = Intent(context, UserActivity::class.java)
            startActivity(intent)
        }

        btnChangeProfile.setOnClickListener {
            val intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra("nick", nickname)
            if (imgUrl.isNotEmpty()) {
                intent.putExtra("imgUrl", imgUrl)
            }
            startActivity(intent)
        }

        return view
    }

    // Image를 가져오는 함수 만들기
    fun getImageData(key : String){
        val storageReference = Firebase.storage.reference.child("$imgUrl.png")

        storageReference.downloadUrl.addOnCompleteListener { task->
            if (task.isSuccessful){
                //Gilde: 웹에 있는 이미지 적용하는 라이브러리
                Glide.with(this)
                    .load(task.result)
                    .into(civProfile) //지역변수
            }
        }
    }

    fun changeInfo() {
        val postListener = (object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (model in snapshot.children) {
                    val item = model.getValue<JoinVO>()
                    if (FBAuth.getUid() == item?.uid) {
                        nickname = item.nick
                        imgUrl = item.profileUrl
                    }
                    getImageData(imgUrl)
                    tvClosetNick.text = nickname+"님의 CLOSET"
                    Log.d("??", "미스테리....")
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        infoRef.addValueEventListener(postListener)
    }
}
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

class ClosetFragment : Fragment() {

    lateinit var infoRef: DatabaseReference
    var nickname: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_closet, container, false)

        val tvClosetNick = view.findViewById<TextView>(R.id.tvClosetNick)
        val btnChangeProfile = view.findViewById<Button>(R.id.btnChangeProfile)
        val btnInfoChange = view.findViewById<Button>(R.id.btnInfoChange)

        infoRef = FBdatabase.getUserRef()

        val postListener = (object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (model in snapshot.children) {
                    val item = model.getValue<JoinVO>()
                    if (FBAuth.getUid() == item?.uid) {
                        nickname = item.nick
                        Log.d("닉네임", nickname)
                    }
                    tvClosetNick.text = nickname+"님의 CLOSET"
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        infoRef.addValueEventListener(postListener)


        btnInfoChange.setOnClickListener {
            val intent = Intent(context, UserActivity::class.java)
            startActivity(intent)
        }

        btnChangeProfile.setOnClickListener {
            val intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra("nick", nickname)
            startActivity(intent)
        }



        return view
    }

}
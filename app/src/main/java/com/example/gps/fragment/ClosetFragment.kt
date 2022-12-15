package com.example.gps.fragment

import android.content.Intent
import android.graphics.Paint.Join
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fullstackapplication.utils.FBAuth
import com.example.fullstackapplication.utils.FBdatabase
import com.example.gps.R
import com.example.gps.board.BoardAdapter
import com.example.gps.board.BoardInsideActivity
import com.example.gps.board.BoardVO
import com.example.gps.board.MyBoardAdapter
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
import kotlinx.android.synthetic.main.fragment_closet.*

class ClosetFragment : Fragment() {
    override fun onResume() {
        super.onResume()
        changeInfo()
    }

    lateinit var infoRef: DatabaseReference
    lateinit var boardRef: DatabaseReference
    lateinit var civProfile: CircleImageView
    lateinit var tvClosetNick: TextView
    lateinit var adapter: MyBoardAdapter
    val myBoardList = ArrayList<BoardVO>()
    var nickname: String = ""
    var imgUrl: String = ""
    val uid = FBAuth.getUid()

    //이미지 정보 넘겨줄 key값을 저장할 배열 만들기
    var keyData = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_closet, container, false)

        tvClosetNick = view.findViewById<TextView>(R.id.tvClosetNick)
        val btnChangeProfile = view.findViewById<Button>(R.id.btnChangeProfile)
        val btnInfoChange = view.findViewById<Button>(R.id.btnInfoChange)
        var tvCntBoard = view.findViewById<TextView>(R.id.tvCntBoard)
        civProfile = view.findViewById(R.id.civProfile)
        val rvMyBoard = view.findViewById<RecyclerView>(R.id.rvMyBoard)

        infoRef = FBdatabase.getUserRef()
        boardRef = FBdatabase.getBoardRef()

        getMyBoardList()

        adapter = MyBoardAdapter(requireContext(), myBoardList)
        rvMyBoard.adapter = adapter

        rvMyBoard.layoutManager = LinearLayoutManager(context)

        adapter.setOnItemClickListener(object : MyBoardAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                //BoardInsideActivity로 넘어가자~

                //상세페이지로 해당 페이지의 값 넘겨주기
                val intent = Intent(requireContext(), BoardInsideActivity::class.java)
                intent.putExtra("title", myBoardList[position].title)
                intent.putExtra("time", myBoardList[position].time)
                intent.putExtra("content", myBoardList[position].content)
                //이미지 key 넘겨주기
                intent.putExtra("key", keyData[position])
                intent.putExtra("uid", myBoardList[position].uid)

                startActivity(intent)
            }

        })

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
    fun getImageData(key: String) {
        val storageReference = Firebase.storage.reference.child("$imgUrl.png")

        storageReference.downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
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
                nickname = snapshot.child(uid).child("nick").value.toString()
                imgUrl = snapshot.child(uid).child("profileUrl").value.toString()

                getImageData(imgUrl)
                tvClosetNick.text = nickname + "님의 CLOSET"
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        infoRef.addValueEventListener(postListener)
    }

    fun getMyBoardList() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                myBoardList.clear()

                for (model in snapshot.children) {
                    val item = model.getValue<BoardVO>()
                    if (item != null && item.uid == FBAuth.getUid()) {
                        myBoardList.add(item)
                        tvCntBoard.text = myBoardList.size.toString()
                    }
                    keyData.add(model.key.toString())
                }
                myBoardList.reverse()
                keyData.reverse()
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        boardRef.addValueEventListener(postListener)
    }

}
package com.example.gps.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide

import com.example.gps.R
import com.example.gps.SplashActivity
import com.example.gps.utils.FBAuth
import com.example.gps.utils.FBAuth.Companion.getUid
import com.example.gps.utils.FBdatabase
import com.google.firebase.database.*


import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_board_inside.*


class BoardInsideActivity : AppCompatActivity() {

    // 게시물의 uid값이 들어갈 가변 배열
    var keyData = ArrayList<String>()
    lateinit var ref: DatabaseReference

    val bookmarkRef = FBdatabase.getBookmarkRef()
    var bookmarkList = ArrayList<BoardVO>()
    var isMark: Boolean = false

    lateinit var imgIn: ImageView
    val database = Firebase.database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_inside)

        //게시글 상세페이지
        val likeRef = database.getReference("like")
        //id값
        val tvInTitle = findViewById<TextView>(R.id.tvInTitle)
        val tvInTime = findViewById<TextView>(R.id.tvInTime)
        val tvInContent = findViewById<TextView>(R.id.tvInContent)

        val btnEdit = findViewById<Button>(R.id.btnEdit)
        val btnRemove = findViewById<Button>(R.id.btnRemove)
        val tvBoardInsideNick = findViewById<TextView>(R.id.tvBoardInsideNick)

        val tvLikeCount = findViewById<TextView>(R.id.tvLikeCount)
        val imgLike = findViewById<ImageView>(R.id.imgLike)
        val imgComment = findViewById<ImageView>(R.id.imgComment)
        val imgBookMark = findViewById<ImageView>(R.id.imgBookMark)
        val id = getUid()

        imgIn = findViewById(R.id.imgIn)


        //해당 게시물의 상세내용을 가져와서 set해주자!
        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        val time = intent.getStringExtra("time")


        //이미지를 Firebase에서 꺼내올 때 사용할 거임
        val key = intent.getStringExtra("key")
        val uid = intent.getStringExtra("uid")

        var like: Boolean = false
        var cnt: Int = 0
        tvInTitle.text = title.toString()
        tvInContent.text = content.toString()
        tvInTime.text = time.toString()

        FBdatabase.getUserRef().child("$uid").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tvBoardInsideNick.text = snapshot.child("nick").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        getImageData(key.toString())

        Log.d("키", key.toString())
        Log.d("키", uid.toString())

//        Log.d("개빡치네",id)
//        Log.d("개빡치네2",uid!!)

        // 좋아요 버튼
        imgLike.setOnClickListener {
            var likeCount = tvLikeCount.text.toString()
            if (like == false) {
                like = true
                imgLike.setImageResource(R.drawable.like)
                cnt++
                tvLikeCount.setText("좋아요 $cnt 개")
                likeRef.push().setValue(likeCount)
            } else {
                like = false
                imgLike.setImageResource(R.drawable.likeup)
                cnt--
                tvLikeCount.setText("좋아요 $cnt 개")
                likeRef.removeValue()
            }


        }

        getBookmarkData(key)
        Log.d("뭘까?", getBookmarkData(key).toString())

        //북마크 칠하기
        imgBookMark.setOnClickListener {
            if (isMark == false) {
                isMark = true
                imgBookMark.setImageResource(R.drawable.mark_black)
                bookmarkRef.child(getUid()).child("$key")
                    .setValue(BoardVO("$title", "$content", "$uid", "$time", "$key"))

            } else {
                isMark = false
                imgBookMark.setImageResource(R.drawable.mark_white)
                bookmarkRef.child(getUid()).child("$key").removeValue()
            }
        }


        imgComment.setOnClickListener {
            val intent = Intent(this@BoardInsideActivity, CommentActivity::class.java)
            intent.putExtra("key", key)
            intent.putExtra("uid", uid)

            startActivity(intent)
        }


        if (id != uid) {

            btnEdit.visibility = View.INVISIBLE
            btnRemove.visibility = View.INVISIBLE
        }


        if (id == uid) {
            btnEdit.setOnClickListener {

                val db = Firebase.database

                // 보드
                val Content = db.getReference("board").child(key.toString())
                Content.setValue(null)


                val intent = Intent(this@BoardInsideActivity, BoardWriteActivity::class.java)
                intent.putExtra("title", title)
                intent.putExtra("content", content)
                intent.putExtra("key", key)


//            intent.putExtra("image",content)
                startActivity(intent)
            }



            btnRemove.setOnClickListener {
                // RealTime Database에 필요한 객체 선언
                val db = Firebase.database

                // 보드
                val Content = db.getReference("board").child(key.toString())
                Content.setValue(null)
//            val mDatabase = FirebaseDatabase.getInstance();
//            val dataRef = mDatabase.getReference("board");

                finish()
            }
        }
    }


    // Image를 가져오는 함수 만들기
    fun getImageData(key: String) {
        val storageReference = Firebase.storage.reference.child("$key.png")

        storageReference.downloadUrl.addOnCompleteListener { task ->
            //task: 데이터를 가져오는데 성공했는지 여부와 데이터 정보를 가지고 있음
            if (task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    //into : imgIn에 업로드 하라는 것!
                    .into(imgIn)

            }
        }
    }

    // 북마크 데이터 받아오기
    fun getBookmarkData(key: String?) {
        val bookmarkListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue<BoardVO>()?.key.toString()

                // firebase 내 저장된 key값(게시글 uid)과
                // 사용자가 클릭한 게시글의 uid를 비교
                if (key != value) {
                    isMark = false
                    imgBookMark.setImageResource(R.drawable.mark_white)
                } else {
                    isMark = true
                    imgBookMark.setImageResource(R.drawable.mark_black)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        }
        FBdatabase.getBookmarkRef().child(getUid()).child(key!!)
            .addValueEventListener(bookmarkListener)
    }

}
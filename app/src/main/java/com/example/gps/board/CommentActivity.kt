package com.example.gps.board

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gps.R
import com.example.gps.utils.FBdatabase.Companion.database
import com.example.gps.utils.FBdatabase.Companion.getBoardRef
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_comment.*
import kotlinx.android.synthetic.main.activity_comment.imgCommentRoomOut
import kotlinx.android.synthetic.main.activity_message.*
import java.text.SimpleDateFormat
import java.util.*

class CommentActivity : AppCompatActivity() {

    private var recyclerView : RecyclerView? = null
    lateinit var nick : String

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        val key = intent.getStringExtra("key")
        val imageView = findViewById<ImageView>(R.id.commentActivity_ImageView)
        val editText = findViewById<TextView>(R.id.commentActivity_editText)
        val imgChatRoomOut = findViewById<ImageView>(R.id.imgCommentRoomOut)

        val userRef=database.getReference("users")

        val uid = intent.getStringExtra("uid")
//        val uid = Firebase.auth.currentUser?.uid.toString()
//        Log.d("현재",uid)
        val key2 =getBoardRef()
        Log.d("게시물 번호?",key2.toString())

        val time = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("MM월dd일 hh:mm")
        val curTime = dateFormat.format(Date(time)).toString()
        // 메세지를 보낸 시간

        recyclerView = findViewById<RecyclerView>(R.id.commentActivity_recyclerview)


        imgChatRoomOut.setOnClickListener{
            finish()
        }

        imageView.setOnClickListener {
            // 댓글 내용 받기
            val com = commentActivity_editText.text.toString()
            Log.d("댓글 내용",com)

            // 닉네임 받기
            val coma = uid!!



            val nick2 = userRef.child(coma).addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
//                    Log.d("값 뽑아오기",snapshot.child("nick").value.toString())
                    nick = snapshot.child("nick").value.toString()
                    Log.d("닉네임",nick)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })






        }


    }
}
package com.example.gps.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.fullstackapplication.utils.FBAuth
import com.example.gps.R
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_comment.*

class CommentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        val key = intent.getStringExtra("key")
        val nick = intent.getStringExtra("nick")
        var cList = ArrayList<CommentVO>()
        val time = FBAuth.getTime()



        // ~ 닉네임,시간, 보드키 값 받아왔다.

        val msg = etComment.text.toString()
        // 달고싶은 댓글

        val db = Firebase.database
        val comment2 = db.getReference("C")
        //데이터베이스에 저장할 공간 만들어놔따

        cList.add(CommentVO(nick.toString(),"" ,time,key.toString()))

        // 배열에 한 댓글에 들어갈 요소 다 넣었지. 이제 이걸 한 댓글템플릿에 넣어보자

        btnComment.setOnClickListener {
            comment2.setValue("arr2")
            Log.d("테스트","왔다")
        }

       // 우선은 세트로 db에 넣는 것부터 해보자
    }
}
//
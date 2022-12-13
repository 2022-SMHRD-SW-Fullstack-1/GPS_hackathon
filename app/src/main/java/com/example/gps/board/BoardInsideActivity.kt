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
import com.bumptech.glide.Glide
import com.example.gps.R
import com.example.gps.SplashActivity
import com.example.gps.utils.FBAuth.Companion.auth
import com.example.gps.utils.FBAuth.Companion.getUid
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class BoardInsideActivity : AppCompatActivity() {



    // 게시물의 uid값이 들어갈 가변 배열
    var keyData = ArrayList<String>()
    lateinit var ref : DatabaseReference

    lateinit var imgIn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_inside)

        //게시글 상세페이지

        //id값
        val tvInTitle = findViewById<TextView>(R.id.tvInTitle)
        val tvInTime = findViewById<TextView>(R.id.tvInTime)
        val tvInContent = findViewById<TextView>(R.id.tvInContent)

        val btnEdit = findViewById<Button>(R.id.btnEdit)
        val btnRemove = findViewById<Button>(R.id.btnRemove)
        val id = getUid()
//
        imgIn = findViewById(R.id.imgIn)



        //해당 게시물의 상세내용을 가져와서 set해주자!
        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        val time = intent.getStringExtra("time")
        val k = intent.getStringExtra("key")

        //이미지를 Firebase에서 꺼내올 때 사용할 거임
        val key = intent.getStringExtra("key")
        val uid = intent.getStringExtra("uid")

        tvInTitle.text = title.toString()
        tvInContent.text = content.toString()
        tvInTime.text = time.toString()

        Log.d("개빡치네",id)
        Log.d("개빡치네2",uid!!)


        //이미지 가져오기(게시물의 uid 값으로 이름을 지정했음)
        //받아온 이미지 key값을 넘겨주기!


        if(id != uid){

                btnEdit.visibility = View.INVISIBLE
                btnRemove.visibility = View.INVISIBLE
            }


        if (id == uid){
        btnEdit.setOnClickListener {

            val db = Firebase.database

            // 보드
            val Content = db.getReference("board").child(k.toString())
            Content.setValue(null)


            val intent = Intent(this@BoardInsideActivity, BoardWriteActivity::class.java)
            intent.putExtra("title",title)
            intent.putExtra("content",content)
            intent.putExtra("key",key)


//            intent.putExtra("image",content)
            startActivity(intent)
        }



        btnRemove.setOnClickListener {
            // RealTime Database에 필요한 객체 선언
            val db = Firebase.database

            // 보드
            val Content = db.getReference("board").child(k.toString())
            Content.setValue(null)
//            val mDatabase = FirebaseDatabase.getInstance();
//            val dataRef = mDatabase.getReference("board");

            finish()
        }



        }




    }




    // Image를 가져오는 함수 만들기
    fun getImageData(key : String){
        val storageReference = Firebase.storage.reference.child("$key.png")

        storageReference.downloadUrl.addOnCompleteListener { task->
            //task: 데이터를 가져오는데 성공했는지 여부와 데이터 정보를 가지고 있음
            if (task.isSuccessful){
                Glide.with(this)
                    .load(task.result)
                    //into : imgIn에 업로드 하라는 것!
                    .into(imgIn)

            }
        }


    }



}
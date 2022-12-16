package com.example.gps.board

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gps.R
import com.example.gps.fragment.ClosetFragment
import com.example.gps.fragment.HomeFragment
import com.example.gps.utils.FBAuth
import com.example.gps.utils.FBdatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class BookmarkActivity : AppCompatActivity() {

    val bookmarkRef = FBdatabase.getBookmarkRef()
    val boardRef = FBdatabase.getBoardRef()

//    var userList = ArrayList<JoinVO>()
    var data = ArrayList<BoardVO>()
    var keyData = ArrayList<String>() // ListVO를 포함하는 게시물의 uid
    var bookmarkList = ArrayList<String>() // 내가 선택한 게시물 uid

    lateinit var adapter: BookmarkAdapter
    var bmCnt : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmark)

        val imgBmBack = findViewById<ImageView>(R.id.imgBmBack)
        val rvBookmark = findViewById<RecyclerView>(R.id.rvBookmark)

        imgBmBack.setOnClickListener {
            finish()
        }

        getBookmarkData()

        adapter = BookmarkAdapter(this, data, bookmarkList, keyData)

        rvBookmark.adapter = adapter
        rvBookmark.layoutManager = LinearLayoutManager(this)
    }

    fun getContentData() {
        // content 경로에 있는 데이터를 다 가지고 오기
        // uid ---> keyData
        // ㄴ> ListVO ---> data
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (model in snapshot.children) {
                    val item = model.getValue(BoardVO::class.java)
                    if (bookmarkList.contains(model.key.toString())) {
                        if (item != null) {
                            data.add(item)
                            // data 내가 북마크 찍은 게시물만 담김
                        }
                    }
                    keyData.add(model.key.toString())
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        boardRef.addValueEventListener(postListener)
        // snapshot : content경로에 있는 데이터 전부
    }

    fun getBookmarkData() {
        // bookmarkList 경로에 있는 데이터를 다 가지고 오자
        // 게시글의 uid값 ---> bookmarkList
        val myBmListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bookmarkList.clear()

                for (model in snapshot.children) {
                    bookmarkList.add(model.key.toString())
                }
                adapter.notifyDataSetChanged()
                getContentData()
                bmCnt = bookmarkList.size.toString()

//                var ClosetFragment = ClosetFragment()
//                var bundle = Bundle()
//
//                bundle.putString("bmCnt", bmCnt)
//                ClosetFragment.arguments = bundle
//                //fragment의 arguments에 데이터를 담은 bundle을 넘겨줌
//
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.fl, ClosetFragment)
//                    .commit()

//                Log.d("북마크사이즈", bundle.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        bookmarkRef.child(FBAuth.getUid()).addValueEventListener(myBmListener)
    }

}
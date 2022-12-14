package com.example.gps.fragment
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gps.R
import com.example.gps.board.CommentAdapter
import com.example.gps.board.CommentVO
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class CommentFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_comment, container, false)

        val etComment = view.findViewById<EditText>(R.id.etComment)
        val btnComment = view.findViewById<Button>(R.id.btnComment)

        //AdapterView 6단계
        //1. Container 결정
        val rvComment = view.findViewById<RecyclerView>(R.id.rvComment)


        //2. Template 결정
        //Comment_list.xml

        //3. Item 결정
        //CommentVO
        val CommentList = ArrayList<CommentVO>()


        //4. Adapter 결정
        //LoginActivity에서 처리해준 채팅 정보 관련
        val sp = activity?.getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
        val loginId = sp?.getString("loginId", "null") as String//로그인 정보 없으면 null

        //CommentAdapter
        val adapter = CommentAdapter(requireContext(), CommentList, loginId)


        //5. Container에 Adapter 부착
        rvComment.adapter = adapter

        //5-1. 레이아웃 매니저 달아주기
        rvComment.layoutManager = LinearLayoutManager(requireContext())

        //6. Event 처리

        //db
        val db = Firebase.database
        //내 DB에 Comment이라는 경로를 만들기
        val commentRef = db.getReference("comment")

        //전송버튼 클릭 시
        btnComment.setOnClickListener {
            val msg = etComment.text.toString()

            //Firebase RealTime Database에 메세지 내용 전송
            //Comment 경로에 CommentVO class를 보낼 거임! => setValue
            val comment = CommentVO(loginId, msg)

            //push()를 안 넣으면 갱신만 됨
            commentRef.push().setValue(comment)

            //etComment.text = "" 이게 안 됨 아래처럼 하면 가능! 아니면 null이라고 써야 함
            etComment.setText("")

        }

        //등록된 메세지 내용 불러와서 화면에 띄우기
        commentRef.addChildEventListener(object : ChildEventListener{
            //object에 있는 추상 메서드 오버라이딩 해주기~
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                val commentItem = snapshot.getValue<CommentVO>() as CommentVO
                CommentList.add(commentItem)
//                Log.d("test",commentItem.msg)
                //어뎁터 새로고침
                adapter.print(commentItem)
                adapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })





        return view

    }

}
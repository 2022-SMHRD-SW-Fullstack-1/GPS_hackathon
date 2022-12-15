package com.example.gps.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fullstackapplication.utils.FBdatabase
import com.example.gps.R
import com.example.gps.board.BoardAdapter
import com.example.gps.board.BoardInsideActivity
import com.example.gps.board.BoardVO
import com.example.gps.board.BoardWriteActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class OotdFragment : Fragment() {

    //getBoardData를 통해 받아온 item(BoardVO)을 관리할 수 있는 배열 선언
    var boardList = ArrayList<BoardVO>()

    lateinit var adapter: BoardAdapter

    //이미지 정보 넘겨줄 key값을 저장할 배열 만들기
    var keyData = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_ootd, container, false)

        val rvBoard = view.findViewById<RecyclerView>(R.id.rvBoard)
        val btnWrite = view.findViewById<Button>(R.id.btnWrite)

        //btnWrite를 클릭하면 BoardWriteActivity로 이동
        btnWrite.setOnClickListener {
            val intent = Intent(requireActivity(), BoardWriteActivity::class.java)//manifests에 경로 확인해서 오류나면 수정해주기~
            startActivity(intent)
        }

        //1. 한 칸에 들어갈 디자인 만들기(board_template.xml)

        //2. Adapter에 보낼 데이터 가져오기
        //Firebase에 있는 board 경로에 있는 데이터 가져오기!
        getBoardData()

        //3. Adapter 만들기
        //위에서 전역변수로 선언할 걸 초기화 시켜준다!
        //data 보내기
        //화면정보를 보내줘야 하는데 여기는 Activitiy가 아니니까 requireContext() 해줘야 함
        adapter = BoardAdapter(requireContext(), boardList)

        //리사이클러뷰 클릭 이벤트 호출
        //상세페이지로 들어가는 애!
        adapter.setOnItemClickListener(object : BoardAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                //BoardInsideActivity로 넘어가자~

                //상세페이지로 해당 페이지의 값 넘겨주기
                val intent = Intent(requireContext(), BoardInsideActivity::class.java)
                intent.putExtra("title", boardList[position].title)
                intent.putExtra("time", boardList[position].time)
                intent.putExtra("content", boardList[position].content)
                //이미지 key 넘겨주기
                intent.putExtra("key", keyData[position])
                intent.putExtra("uid", boardList[position].uid)



                startActivity(intent)

            }

        })
        //4. rvBoard에 Adapter 적용
        rvBoard.adapter = adapter

        //4-1. 레이아웃 매니저
        //일렬로 정렬
        rvBoard.layoutManager = LinearLayoutManager(requireContext())

        return view
    }

    //2번에서 사용할 함수(board에 있는 데이터 다~ 가져오는 함수)
    fun getBoardData(){

        val postListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //Firebase에서 snapshot으로 데이터를 받아온 경우
                //들어오는 형식
                //게시물의 uid(key)
                //- BoardVo(value)

                boardList.clear()

                //model에 BoardVO 넣어주기!
                for (model in snapshot.children){
                    //item에 BoardVO 타입으로 가져오는 값을 담아준다
                    val item = model.getValue(BoardVO::class.java)

                    if (item != null) {
                        //가져온 item을 배열에 담기!
                        //null값일 수도 있기 때문에 null 처리 해주는 것
                        boardList.add(item)
                    }

                    //이미지가 key값으로 이름이 설정되어 있기 때문에 이미지 정보를 key값을 넘겨줘야 함!
                    keyData.add(model.key.toString())

                }
                //게시글 사진 순서 정렬을 위해
                //실행하니까 최근에 등록한 내용이 위로 올라옴
                boardList.reverse()
                keyData.reverse()
                //Adapter 새로고침하기
                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                //오류가 발생했을 경우 실행되는 함수
            }

        }
        //board에 있는 모든 데이터가 들어감
        FBdatabase.getBoardRef().addValueEventListener(postListener)
    }

}
//
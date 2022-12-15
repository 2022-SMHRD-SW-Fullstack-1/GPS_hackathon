package com.example.gps.board


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gps.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

//Fragment3에 있는 rvBoard에 적용될 Adapter
class BoardAdapter(
    var context: Context,
    var boardList: ArrayList<BoardVO>,
//    var keyData: ArrayList<String>
)
    :RecyclerView.Adapter<BoardAdapter.ViewHolder>() {

    //리사이클러뷰 항목을 클릭할 때 이벤트를 넣고 싶을 때 인터페이스 필요(내가 설계해야 하는 거임)
    //인터페이스
    //리스너 커스텀(준비)
    interface OnItemClickListener{
        fun onItemClick(view: View, position: Int)
    }

    //객체 저장 변수 선언
    lateinit var mOnItemClickListener: OnItemClickListener

    //객체 전달 메서드
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        mOnItemClickListener = onItemClickListener
    }

//    val database=Firebase.database
//    val auth : FirebaseAuth=Firebase.auth

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val tvRvTitle: TextView
        val tvRvContent: TextView
        val tvRvTime: TextView


        init {
            tvRvTitle = itemView.findViewById(R.id.tvRvTitle)
            tvRvContent = itemView.findViewById(R.id.tvRvContent)
            tvRvTime = itemView.findViewById(R.id.tvRvTime)

            //리스너 커스텀 초기화
            itemView.setOnClickListener {
                val position = adapterPosition//클릭한 위치를 알려줌
                if (position != RecyclerView.NO_POSITION){
                    //-1이 아닐 경우에 itemView, position 보내기 => 리사이클러 뷰 버그로 가끔 -1을 보내줌 ㅠ.ㅠ
                    mOnItemClickListener.onItemClick(itemView, position)
                }
            }
        }//init 닫힘

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.board_template, null)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvRvTitle.text = boardList[position].title
        holder.tvRvContent.text = boardList[position].content
        holder.tvRvTime.text = boardList[position].time


    }

    override fun getItemCount(): Int {
        return boardList.size //항목의 개수
    }

}//
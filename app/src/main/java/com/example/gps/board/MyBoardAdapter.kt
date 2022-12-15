package com.example.gps.board

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gps.R

class MyBoardAdapter(val context: Context, val myBoardList: ArrayList<BoardVO>)
    : RecyclerView.Adapter<MyBoardAdapter.ViewHolder>() {

    //listener 커스텀 필요
    interface  OnItemClickListener{
        fun onItemClick(view : View, position: Int, )
    }

    //객체 저장 변수 선언
    lateinit var mOnItemClickListener : OnItemClickListener

    //객체 전달 메서드 설계
    fun setOnItemClickListener(onItemClickListener:OnItemClickListener){
        mOnItemClickListener = onItemClickListener
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvBoardTitle : TextView
        val tvBoardTime : TextView

        init {
            tvBoardTitle = itemView.findViewById(R.id.tvBoardTitle)
            tvBoardTime = itemView.findViewById(R.id.tvBoardTime)

            itemView.setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
                    mOnItemClickListener.onItemClick(itemView, position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.my_board_list, null)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvBoardTitle.setText(myBoardList.get(position).title)
        holder.tvBoardTime.setText(myBoardList.get(position).time)

    }

    override fun getItemCount(): Int {
        return myBoardList.size
    }
}
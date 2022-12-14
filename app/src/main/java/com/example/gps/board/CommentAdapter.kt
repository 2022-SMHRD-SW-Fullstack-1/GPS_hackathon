package com.example.gps.board

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gps.R

class CommentAdapter(val context: Context, val commentList: ArrayList<CommentVO>, val loginId: String): RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    lateinit var tvName : String
    lateinit var tvMsg : String

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val tvName : TextView
        val tvMsg : TextView



        init {

            tvName = itemView.findViewById(R.id.tvName)
            tvMsg = itemView.findViewById(R.id.tvMsg)

//            tvMsg.setText(commentList[position].msg)
//            tvName.setText(commentList[position].name)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.comment_list, null)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val name = commentList[position].name

        if(loginId == name){


        }else{

        }

    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    fun print(commentList: CommentVO){
        Log.d("제발",commentList.msg)

    }
}
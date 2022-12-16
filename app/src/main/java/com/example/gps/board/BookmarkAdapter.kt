package com.example.gps.board

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gps.R
import com.example.gps.user.JoinVO

class BookmarkAdapter(
    val context: Context,
    val bookmarkList: ArrayList<BoardVO>,
    val key: ArrayList<String>,
    val keyData: ArrayList<String>)
    :RecyclerView.Adapter<BookmarkAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvBmBoardTitle : TextView
        val tvBmTime : TextView
        val ivBmBookmark : ImageView
        val ivBmImg : ImageView

        init {
            tvBmTime = itemView.findViewById(R.id.tvBmTime)
            tvBmBoardTitle = itemView.findViewById(R.id.tvBmBoardTitle)
            ivBmBookmark = itemView.findViewById(R.id.ivBmBookmark)
            ivBmImg = itemView.findViewById(R.id.ivBmImg)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.bookmark_list, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvBmBoardTitle.setText(bookmarkList.get(position).title)
        holder.tvBmTime.setText(bookmarkList.get(position).time)
        holder.ivBmBookmark.setImageResource(R.drawable.mark_black)

        Glide.with(context)
            .load(bookmarkList[position].uid)
            .into(holder.ivBmImg)
    }

    override fun getItemCount(): Int {
        return bookmarkList.size
    }

}
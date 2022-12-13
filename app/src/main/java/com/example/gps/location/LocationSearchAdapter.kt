package com.example.gps.location

import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class LocationSearchAdapter(): RecyclerView.Adapter<LocationSearchAdapter.ViewHolder>() {

    private var searchResultList: List<SearchResultEntity> = listOf()
    var currentPage = 1
    var currentSearchString = ""

    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){



    }

//    private lateinit var searchResultClickListener: (SearchResultEntity) -> Unit
//    inner class ViewHolder(
//        private val binding: ViewHolderSearchResultItemBinding,
//        private val searchResultClickListener: (SearchResultEntity) -> Unit
//    ): RecyclerView.ViewHolder(hinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

}
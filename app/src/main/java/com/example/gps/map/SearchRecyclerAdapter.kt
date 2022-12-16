package com.example.gps.map

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gps.databinding.ListLayoutBinding

class SearchRecyclerAdapter : RecyclerView.Adapter<SearchRecyclerAdapter.SearchResultViewHolder>() {

    private var searchResultList: List<SearchResultEntity> = listOf()
    var currentPage = 1
    var currentSearchString = ""

    private lateinit var searchResultClickListener: (SearchResultEntity) -> Unit

    inner class SearchResultViewHolder(
        private val binding: ListLayoutBinding,
        private val searchResultClickListener: (SearchResultEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: SearchResultEntity) = with(binding) {
            tvMapName.text = data.name
            tvMapRoad.text = data.fullAddress
        }

        fun bindViews(data: SearchResultEntity) {
            binding.root.setOnClickListener {
                searchResultClickListener(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val binding = ListLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchResultViewHolder(binding, searchResultClickListener)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bindData(searchResultList[position])
        holder.bindViews(searchResultList[position])
    }

    override fun getItemCount(): Int {
        return searchResultList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSearchResultList(
        searchResultList: List<SearchResultEntity>,
        searchResultClickListener: (SearchResultEntity) -> Unit
    ) {
        this.searchResultList = this.searchResultList + searchResultList
        this.searchResultClickListener = searchResultClickListener
        notifyDataSetChanged()
    }

    fun clearList(){
        searchResultList = listOf()
    }

}
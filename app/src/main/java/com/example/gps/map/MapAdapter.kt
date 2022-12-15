//package com.example.gps.map
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.gps.R
//import com.google.android.gms.maps.GoogleMap
//
//class MapAdapter : RecyclerView.Adapter<MapAdapter.ViewHolder>() {
//
//    private var searchResultList: List<SearchResultEntity> = listOf()
//    var currentPage = 1
//    var currentSearchString = ""
//
//    private lateinit var searchResultClickListener: (SearchResultEntity) -> Unit
//
//
//    inner class ViewHolder(private val binding: ViewholderSearchResultItemBinding,
//                           private val searchResultClickListener: (SearchResultEntity) -> Unit
//    ) : RecyclerView.ViewHolder(binding.root) {
//        fun bindData(data: SearchResultEntity) = with(binding) {
//            tvMapName.text = data.name
//            tvMapRoad.text = data.fullAddress
//        }
//
//        fun bindViews(data: SearchResultEntity) {
//            binding.root.setOnClickListener {
//                searchResultClickListener(data)
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//
//
//
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//
//
//
//
//    }
//
//    override fun getItemCount(): Int {
//
//        return searchResultList = listOf()
//
//    }
//
//}
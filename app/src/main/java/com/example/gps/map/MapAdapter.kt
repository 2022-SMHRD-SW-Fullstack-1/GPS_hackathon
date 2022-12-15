//package com.example.gps.map
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.gps.R
//import com.google.android.gms.maps.GoogleMap
//
//class MapAdapter(val context: Context, val mapList: ArrayList<MapVO>): RecyclerView.Adapter<MapAdapter.ViewHolder>() {
//
//
//    //리사이클러뷰 항목 클릭 시 필요한 인터페이스(리스너 커스텀)
//    interface OnItemClickListener{
//        fun onItemClick(View: View, position: Int)
//    }
//
//    //객체 저장 변수 선언
//    lateinit var mOnItemClickListener: OnItemClickListener
//
//    //객체 전달 메서드
//    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
//        mOnItemClickListener = onItemClickListener
//    }
//
//
//    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
//        val tvMapName: TextView
//        val tvMapRoad: TextView
//        val tvMapAddress: TextView
//
//        init {
//            tvMapName = itemView.findViewById(R.id.tvMapName)
//            tvMapRoad = itemView.findViewById(R.id.tvMapRoad)
//            tvMapAddress = itemView.findViewById(R.id.tvMapAddress)
//
//            itemView.setOnClickListener {
//                val position = adapterPosition//클릭한 위치 알려줌
//                if(position != RecyclerView.NO_POSITION){
//                    mOnItemClickListener.onItemClick(itemView, position)
//                }
//            }
//
//        }
//
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//
//        val layoutInflater = LayoutInflater.from(context)
//        val view = layoutInflater.inflate(R.layout.list_layout, null)
//
//        return ViewHolder(view)
//
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//
//        holder.tvMapName.text = mapList[position].name
//        holder.tvMapRoad.text = mapList[position].road
//        holder.tvMapAddress.text = mapList[position].address
//
//
//    }
//
//    override fun getItemCount(): Int {
//        return mapList.size
//    }
//
//}
//package com.example.gps.user
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.EditText
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.gps.R
//import com.google.firebase.auth.ktx.auth
//import com.google.firebase.ktx.Firebase
//
//class UserAdapter(
//    val context: Context,
//    val userList: ArrayList<UserVO>
//) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val tvInfoEmail: TextView
//        val etInfoPw: EditText
//        val etInfoPwCk: EditText
//        val etInfoNick: EditText
//        val ivInfoProfile: ImageView
//        val btnInfoOk: Button
//        val btnInfoDelAcc: Button
//
//        init {
//            tvInfoEmail = itemView.findViewById(R.id.tvInfoEmail)
//            etInfoPw = itemView.findViewById(R.id.etInfoPw)
//            etInfoPwCk = itemView.findViewById(R.id.etInfoPwCk)
//            etInfoNick = itemView.findViewById(R.id.etInfoNick)
//            ivInfoProfile = itemView.findViewById(R.id.ivInfoProfile)
//            btnInfoDelAcc = itemView.findViewById(R.id.btnInfoDelAcc)
//            btnInfoOk = itemView.findViewById(R.id.btnInfoOk)
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val layoutInflater = LayoutInflater.from(context)
//        val view = layoutInflater.inflate(R.layout.user_info, null)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.tvInfoEmail.setText(userList[position].email)
//        holder.etInfoNick.setText(userList[position].nick)
////        holder.ivInfoProfile.setImageResource(userList.get(position).profilePic)
////        Glide.with(context)
////            .load(userList[position].profilePic)
////            .into(holder.ivInfoProfile)
//
//        holder.btnInfoOk.setOnClickListener {
//            val pw = holder.etInfoPw.text.toString()
//            val pwCk = holder.etInfoPwCk.text.toString()
//            if (pw == pwCk && pw.length >= 8) {
//                val user = Firebase.auth.currentUser
//
//
//            }
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return userList.size
//    }
//
//
//}
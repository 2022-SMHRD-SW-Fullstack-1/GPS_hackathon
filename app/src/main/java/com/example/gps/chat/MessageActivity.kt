package com.example.gps.chat

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.gps.chat.ChatModel.Comment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_message.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MessageActivity : AppCompatActivity() {

    private val fireDatabase = FirebaseDatabase.getInstance().reference
    private var chatRoomUid : String? = null
    private var destinationUid : String? = null
    private var uid : String? = null
    private var recyclerView : RecyclerView? = null

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.gps.R.layout.activity_message)
        val imageView = findViewById<ImageView>(com.example.gps.R.id.messageActivity_ImageView)
        val editText = findViewById<TextView>(com.example.gps.R.id.messageActivity_editText)
        val imgChatRoomOut = findViewById<ImageView>(com.example.gps.R.id.imgMessageRoomOut)

        //메세지를 보낸 시간
        val time = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("MM월dd일 hh:mm")
        val curTime = dateFormat.format(Date(time)).toString()


        destinationUid = intent.getStringExtra("destinationUid")
        uid = Firebase.auth.currentUser?.uid.toString()
        recyclerView = findViewById(com.example.gps.R.id.messageActivity_recyclerview)

        imgChatRoomOut.setOnClickListener{
            val intent = Intent(this@MessageActivity, ChatActivity::class.java)
            startActivity(intent)
            finish()
        }

        imageView.setOnClickListener {
            Log.d("클릭 시 dest", "$destinationUid")
            val chatModel = ChatModel()
            chatModel.users.put(uid.toString(), true)
            chatModel.users.put(destinationUid!!, true)

            val comment = Comment(uid, editText.text.toString(), curTime)
            if(chatRoomUid == null){
                imageView.isEnabled = false
                fireDatabase.child("chatrooms").push().setValue(chatModel).addOnSuccessListener {
                    //채팅방 생성
                    checkChatRoom()
                    //메세지 보내기
                    Handler().postDelayed({
                        println(chatRoomUid)
                        fireDatabase.child("chatrooms").child(chatRoomUid.toString()).child("comments").push().setValue(comment)
                        messageActivity_editText.text = null
                    }, 1000L)
                    Log.d("chatUidNull dest", "$destinationUid")
                }
            }else{
                fireDatabase.child("chatrooms").child(chatRoomUid.toString()).child("comments").push().setValue(comment)
                messageActivity_editText.text = null
                Log.d("chatUidNotNull dest", "$destinationUid")
            }
        }
        checkChatRoom()
    }

    private fun checkChatRoom(){
        fireDatabase.child("chatrooms").orderByChild("users/$uid").equalTo(true)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (item in snapshot.children){
                        println(item)
                        val chatModel = item.getValue<ChatModel>()
                        if(chatModel?.users!!.containsKey(destinationUid)){
                            chatRoomUid = item.key
                            messageActivity_ImageView.isEnabled = true
                            recyclerView?.layoutManager = LinearLayoutManager(this@MessageActivity)
                            recyclerView?.adapter = RecyclerViewAdapter()
                        }
                    }
                }
            })
    }

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.MessageViewHolder>() {

        private val comments = ArrayList<Comment>()
        private var friend : Friend? = null
        init{
            fireDatabase.child("users").child(destinationUid.toString()).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    friend = snapshot.getValue<Friend>()
                    messagetActivity_textView_topName.text = friend?.nick
                    getMessageList()
                }
            })
        }

        fun getMessageList(){
            fireDatabase.child("chatrooms").child(chatRoomUid.toString()).child("comments").addValueEventListener(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    comments.clear()
                    for(data in snapshot.children){
                        val item = data.getValue<Comment>()
                        comments.add(item!!)
                        println(comments)
                    }
                    notifyDataSetChanged()
                    //메세지를 보낼 시 화면을 맨 밑으로 내림
                    recyclerView?.scrollToPosition(comments.size - 1)
                }
            })
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
            val view : View = LayoutInflater.from(parent.context).inflate(com.example.gps.R.layout.item_message, parent, false)

            return MessageViewHolder(view)
        }
        @SuppressLint("RtlHardcoded")
        override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
            holder.textView_message.textSize = 20F
            holder.textView_message.text = comments[position].message
            holder.textView_time.text = comments[position].time
            if(comments[position].uid.equals(uid)){ // 본인 채팅
                holder.textView_message.setBackgroundResource(com.example.gps.R.drawable.rightbubble)
                holder.textView_name.visibility = View.INVISIBLE
                holder.layout_destination.visibility = View.INVISIBLE
                holder.layout_main.gravity = Gravity.RIGHT
            }else{ // 상대방 채팅
                val storageReference = Firebase.storage.reference.child("${friend?.profileUrl}.png")

                storageReference.downloadUrl.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Glide.with(holder.itemView.context)
                            .load(task.result).apply(RequestOptions().circleCrop())
                            .into(holder.imageView_profile)
                    }
                }
                holder.textView_name.text = friend?.nick
                holder.layout_destination.visibility = View.VISIBLE
                holder.textView_name.visibility = View.VISIBLE
                holder.textView_message.setBackgroundResource(com.example.gps.R.drawable.leftbubble)
                holder.layout_main.gravity = Gravity.LEFT
            }
        }

        inner class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textView_message: TextView = view.findViewById(com.example.gps.R.id.tvCommentMessage)
            val textView_name: TextView = view.findViewById(com.example.gps.R.id.tvCommentName)
            val imageView_profile: ImageView = view.findViewById(com.example.gps.R.id.messageItem_imageview_profile)
            val layout_destination: LinearLayout = view.findViewById(com.example.gps.R.id.messageItem_layout_destination)
            val layout_main: LinearLayout = view.findViewById(com.example.gps.R.id.messageItem_linearlayout_main)
            val textView_time : TextView = view.findViewById(com.example.gps.R.id.tvCommentTime)
        }

        override fun getItemCount(): Int {
            return comments.size
        }
    }
}
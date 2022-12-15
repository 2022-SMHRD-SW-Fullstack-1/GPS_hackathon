package com.example.gps.user

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.fullstackapplication.utils.FBAuth
import com.example.fullstackapplication.utils.FBdatabase
import com.example.gps.R
import com.example.gps.fragment.ClosetFragment
import com.example.gps.fragment.HomeFragment
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.output.ByteArrayOutputStream
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import de.hdodenhof.circleimageview.CircleImageView

class ProfileActivity : AppCompatActivity() {

    lateinit var infoRef: DatabaseReference
    lateinit var civSelectProfile : CircleImageView
    lateinit var imgUrl: String
    val uid = FBAuth.getUid()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val etInfoNick = findViewById<EditText>(R.id.etInfoNick)
        val imgChangeProfile = findViewById<ImageView>(R.id.imgChangeProfile)
        civSelectProfile = findViewById(R.id.civSelectProfile)
        val imgProfileBack = findViewById<ImageView>(R.id.imgProfileBack)

        infoRef = FBdatabase.getUserRef()

        val currentNick = intent.getStringExtra("nick")
        imgUrl = intent.getStringExtra("imgUrl").toString()
        Log.d("프로필닉네임", currentNick.toString())

        etInfoNick.setText(currentNick)
        getImageData(imgUrl)

        imgProfileBack.setOnClickListener {
            onBackPressed()
        }

        civSelectProfile.setOnClickListener {
                //사진 데이터 준비
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                launcher.launch(intent)
        }

        imgChangeProfile.setOnClickListener {

            val newNick = etInfoNick.text.toString()

            // 프로필 사진 수정
            val profilePostListener = (object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (model in snapshot.children) {
                        val item = model.getValue<JoinVO>()
                        val itemKey = model.key
                        if (FBAuth.getUid() == item?.uid) {
                            if (itemKey != null) {
                                var key = itemKey.toString()
                                Log.d("key값", key)
                                infoRef.child(itemKey).child("profileUrl").setValue(key)
                                imgUpload(key)
                                finish()
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
            infoRef.addValueEventListener(profilePostListener)


            // 닉네임 수정
            if (newNick.isNotEmpty()) {
                // Firebase에서 데이터를 받아오는 Listener
                val postListener = (object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (model in snapshot.children) {
                            val item = model.getValue<JoinVO>()
                            val itemKey = model.key
                            if (FBAuth.getUid() == item?.uid) {
                                if (itemKey != null) {
                                    infoRef.child(itemKey).child("nick").setValue(newNick)
                                    Toast.makeText(
                                        this@ProfileActivity,
                                        "닉네임이 변경되었습니다 $newNick",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
                infoRef.addValueEventListener(postListener)
            }
            onBackPressed()
        }
    }


    //StartActivityForResult:결과값을 받아오기
    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        //받아올 결과값이 맞는지 확인 과정
        if (it.resultCode == RESULT_OK) civSelectProfile.setImageURI(it.data?.data)

    }

    //key()값으로 변수로 사용.
    fun imgUpload(key : String) {

        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child("$key.png")

        civSelectProfile.isDrawingCacheEnabled = true
        civSelectProfile.buildDrawingCache()
        val bitmap = (civSelectProfile.drawable as BitmapDrawable?)?.bitmap
        val baos = ByteArrayOutputStream()
        //quality:압축 퀄리티 1~100.
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val data = baos.toByteArray()

        //mountainsRef : 스토리지 경로 지정하는 키워드.
        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }

    fun getImageData(imgUrl : String){
        val storageReference = Firebase.storage.reference.child("$imgUrl.png")

        storageReference.downloadUrl.addOnCompleteListener { task->
            if (task.isSuccessful){
                //Gilde: 웹에 있는 이미지 적용하는 라이브러리
                Glide.with(this)
                    .load(task.result)
                    .into(civSelectProfile) //지역변수
            }
        }
    }


}
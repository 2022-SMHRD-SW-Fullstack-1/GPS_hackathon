package com.example.gps.board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.gps.R
import com.example.gps.utils.FBAuth
import com.example.gps.utils.FBdatabase
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.output.ByteArrayOutputStream
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class BoardWriteActivity : AppCompatActivity() {

    lateinit var imgLoad: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_write)

        //id값 다 찾아오기
        imgLoad = findViewById(R.id.imgLoad)
        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etContent = findViewById<EditText>(R.id.etContent)
        val imgWrite = findViewById<ImageView>(R.id.imgWrite)

        //이미지 불러오기
        //갤러리로 이동해서 이미지를 받아오는 역할
        imgLoad.setOnClickListener {
            //갤러리로 이동하기
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)

            launcher.launch(intent)
        }

        //db랑 auth를 관리하는 패키지안에 관련 내용 클래스들을 만들어서 호출해서 사용하자!

        //모든 값(title, content, time, image)을 Firebase에 저장하는 역할
        imgWrite.setOnClickListener {
            val title = etTitle.text.toString()
            val content = etContent.text.toString()

            //board
            //-key(게시글의 고유한 uid: push())
            //push(): 고유한 uid 부여하는 애
            //- boardVO(title, content, 사용자 uid, time)
            //잘 들어가는지 확인해보자~
            //            FBdatabase.getBoardRef().push().setValue(BoardVO("1", "1","1","1"))

            //auth
            //FBAuth
            //사용자 uid 가져오기
            val uid = FBAuth.getUid()
            //현재 시간을 가지고 올 수 있는 캘린더
            val time = FBAuth.getTime()

            //setValue가 되기 전에 미리 BoardVO가 저장될 key값 설정!(uid값을 만들자)
            var key = FBdatabase.getBoardRef().push().key.toString()//uid값을 먼저 만들어줌

            //DB에 게시글 작성한 내용 보내기
            //먼저 만들어진 uid를 게시글에 부착!
            FBdatabase.getBoardRef().child(key).setValue(BoardVO(title, content, uid, time))

            //부착된 키값을 이미지에도 같이 부착!
            imgUpload(key)
            finish()//이전페이지로 돌아가기 위해!

        }


    }//onCreate 닫힘

    //런처 선언
    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        //위에서 갤러리를 불러올 때 사진을 URI 값으로 받아오기 때문에 URI로 찾아오는 거임!
        if(it.resultCode == RESULT_OK){
            //이미지 미리보기
            imgLoad.setImageURI(it.data?.data)
        }

    }

    //이미지 업로드
    fun imgUpload(key: String){

        val storage = Firebase.storage
        val storageRef = storage.reference
        //받아준 uid(key)값으로 이미지 이름 저장!
        val mountainsRef = storageRef.child("$key.png")//아래에서 받는 jpge 타입을 png 타입으로 변환하는 거임
        //꼭! png 파일로 변환해야 함!!!!!

        imgLoad.isDrawingCacheEnabled = true
        imgLoad.buildDrawingCache()
        val bitmap = (imgLoad.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()

        //사진 압축하는 애
        //(압축하는 애의 타입, 압축의 퀄리티(1~100-보통 50~60으로 설정함),)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val data = baos.toByteArray()

        //이미지를 비트맵형태로 가지고 옴 => png로 가져오게 됨
        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }


    }
}
package com.example.gps.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.gps.R

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        //받아온 Url 값을 내서
        // 해당 웹페이지가 WebView에 뜨게 만들기

        //1. 주소 준비
        val wv=findViewById<WebView>(R.id.wv)
        val webUrl=intent.getStringExtra("url")

        //2. 설정변경(javaScript지원)
        val ws= wv.settings
        ws.javaScriptEnabled=true

        //3. WebViewClient 설정
        wv.webViewClient= WebViewClient()
        //4. 주소 적용
        //intent로 부터 값을 무조건 받아오는 이유
        // 값이 없으면 RecyclerView에서 안보임( 실행이 안됨 )
        wv.loadUrl(webUrl!!)
    }
}
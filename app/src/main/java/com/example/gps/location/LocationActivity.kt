package com.example.gps.location

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.gps.R
import com.example.gps.databinding.ActivityLocationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LocationActivity : AppCompatActivity() {

    lateinit var binding : ActivityLocationBinding


    companion object {
        const val BASE_URL = "https://dapi.kakao.com/"
        const val API_KEY = "KakaoAK 28005f1b57ad12007b0b722c97f79e6a" // REST API 키
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_location)

        val etSearchLocation = findViewById<EditText>(R.id.etLocationSearch)
        val btnSearchLocation = findViewById<Button>(R.id.btnLocationSearch)

        var searchLocation = etSearchLocation.toString()

        btnSearchLocation.setOnClickListener {

        }

    }






}
package com.example.gps.location

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.gps.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest

class LocationActivity : AppCompatActivity() {

    //현재위치를 가져오기 위한 변수
    lateinit var fusedLocationClient: FusedLocationProviderClient

    lateinit var lastLocation: Location // 위치 값을 가지고 있는 객체

    internal lateinit var mLocationRequest: LocationRequest // 위치 정보 요청의 매개변수를 저장하는

    val REQUEST_PERMISSION_LOCATION = 10

    lateinit var btnLocation: Button
    lateinit var tvMyLocation: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        btnLocation = findViewById(R.id.btnMyLocation)
        tvMyLocation = findViewById(R.id.tvMyLocation)



    }
}
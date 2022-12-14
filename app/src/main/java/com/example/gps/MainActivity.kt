package com.example.gps

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gps.fragment.*
import com.example.gps.weather.WeatherVO
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //프로젝트 키 해시 가져오는 코드
//        try {
//            val information =
//                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
//            val signatures = information.signingInfo.apkContentsSigners
//            val md = MessageDigest.getInstance("SHA")
//            for (signature in signatures) {
//                val md: MessageDigest
//                md = MessageDigest.getInstance("SHA")
//                md.update(signature.toByteArray())
//                var hashcode = String(Base64.encode(md.digest(), 0))
//                Log.d("hashcode", "" + hashcode)
//            }
//        } catch (e: Exception) {
//            Log.d("hashcode", "에러::" + e.toString())
//        }

        val imgWeather = findViewById<ImageView>(R.id.imgWeather)
        val tvWeather = findViewById<TextView>(R.id.tvWeather)
        val bnv = findViewById<BottomNavigationView>(R.id.bnv)
        val fl = findViewById<FrameLayout>(R.id.fl)
        val imgLogout = findViewById<ImageView>(R.id.imgLogout)
        val tvMap = findViewById<TextView>(R.id.tvMap)

        val weatherList = ArrayList<WeatherVO>()

        tvMap.setOnClickListener {
            val intent = Intent(this@MainActivity, MapActivity::class.java)
            startActivity(intent)
        }

        supportFragmentManager.beginTransaction().replace(
            R.id.fl,
            HomeFragment()
        ).commit()

        //Volley 네트워크 통신 4단계
        //1. Volley 라이브러리 설정
        //1-1. 라이브러리 추가, 인터넷 권한, 요청 방식이 http 인지 https인지 !
        //1-2. http면 manifests에 android:usesCleartextTraffic="true" 추가 하기!
        //2. RequestQueue 생성
        val requestQueue = Volley.newRequestQueue(this@MainActivity)

        //3. Request 생성
        val cityList = ArrayList<String>()
        cityList.add("Gwangju")

        val requestList = ArrayList<StringRequest>()

        val index = 0

        val url =
            "https://api.openweathermap.org/data/2.5/weather?q=${cityList[index]}&appid=0191cb6e487f18ad2edd5dcf1dc5e49a&units=metric"

        val request = StringRequest(
            Request.Method.GET,
            url,
            { response ->
                Log.d("날씨 $index", response)
                //weather[0] → main
                val result = JSONObject(response)
                val weathers = result.getJSONArray("weather")
                val weather = weathers[0] as JSONObject //as JSONObject JSONObject로 다운캐스팅한다는 뜻
                val state = weather.getString("main")

                //main → temp, humidity
                val main = result.getJSONObject("main")
                val temp = main.getString("temp")

                Log.d("현재날씨$index", "상태: $state, 온도: $temp")
                weatherList.add(WeatherVO(cityList[index], state, temp))
            },
            {}
        )
        requestList.add(request)


        for (i in 0 until requestList.size) {
            requestQueue.add(requestList[i])
        }

        imgWeather.setOnClickListener {
            if(weatherList.get(0).state == "Clear"){
                imgWeather.setImageResource(R.drawable.sunny)
            }else if(weatherList.get(0).state == "Snow"){
                imgWeather.setImageResource(R.drawable.snow)
            }else if (weatherList.get(0).state == "Clouds"){
                imgWeather.setImageResource(R.drawable.cloudy)
            }else if(weatherList.get(0).state == "Rain"){
                imgWeather.setImageResource(R.drawable.rain)
            }
            tvWeather.text = weatherList.get(0).temp
        }

        bnv.setOnItemSelectedListener { item ->
            // item -> 내가 선택한 item의 정보
            Log.d("id", item.itemId.toString())
            when (item.itemId) {
                R.id.tap1 -> {
                    // Fragment1Home 부분화면으로 갈아끼워준다
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fl,
                        RankFragment()
                    ).commit()
                }
                R.id.tap2 -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fl,
                        AdviseFragment()
                    ).commit()
                }
                R.id.tap3 -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fl,
                        HomeFragment()
                    ).commit()
                }
                R.id.tap4 -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fl,
                        OotdFragment()
                    ).commit()
                }
                R.id.tap5 -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fl,
                        ClosetFragment()
                    ).commit()
                }
            }
            true
        }


    }


}
package com.example.gps.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gps.R
import com.example.gps.weather.WeatherVO
import org.json.JSONObject

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)


        val imgWeatherState = view.findViewById<ImageView>(R.id.imgWeatherState)
        val tvWeatherTemp = view.findViewById<TextView>(R.id.tvTemp)
        val tvCity = view.findViewById<TextView>(R.id.tvCity)
        val tvState = view.findViewById<TextView>(R.id.tvState)
        val tvToday = view.findViewById<TextView>(R.id.tvToday)
        val imgToday = view.findViewById<ImageView>(R.id.imgToday)
//        val flWeather = view.findViewById<FrameLayout>(R.id.flWeather)

        val weatherList = ArrayList<WeatherVO>()

        //Volley 네트워크 통신 4단계
        //1. Volley 라이브러리 설정
        //1-1. 라이브러리 추가, 인터넷 권한, 요청 방식이 http 인지 https인지 !
        //1-2. http면 manifests에 android:usesCleartextTraffic="true" 추가 하기!
        //2. RequestQueue 생성
        val requestQueue = Volley.newRequestQueue(requireContext())


        //3. Request 생성
        val cityList = ArrayList<String>()
        cityList.add("Gwangju")
//        cityList.add("Seoul")

        val requestList = ArrayList<StringRequest>()

        for (i in 0 until cityList.size) {
            val url =
                "https://api.openweathermap.org/data/2.5/weather?q=${cityList[i]}&appid=0191cb6e487f18ad2edd5dcf1dc5e49a&units=metric"

            val request = StringRequest(
                Request.Method.GET,
                url,
                { response ->
                    Log.d("날씨 $i", response)
                    //weather[0] → main
                    val result = JSONObject(response)
                    val weathers = result.getJSONArray("weather")
                    val weather = weathers[0] as JSONObject //as JSONObject JSONObject로 다운캐스팅한다는 뜻
                    val state = weather.getString("main")

                    //main → temp, humidity
                    val main = result.getJSONObject("main")
                    val temp = main.getString("temp")

                    Log.d("현재날씨$i", "상태: $state, 온도: $temp")
                    weatherList.add(WeatherVO(cityList[i], state, temp))
                },
                {}
            )
            requestList.add(request)

        }

        for (i in 0 until requestList.size) {
            requestQueue.add(requestList[i])
        }

        //필요없는 정보 안 보이게 하기
        tvCity.visibility = View.INVISIBLE
        tvState.visibility = View.INVISIBLE

        //처음 화면에 아무것도 안 보이게 하기


        view.setOnClickListener {
            var state = weatherList.get(0).state
            var temp = weatherList.get(0).temp

            if (state == "Clear") {
                imgWeatherState.setImageResource(R.drawable.sunny)
            } else if (state == "Snow") {
                imgWeatherState.setImageResource(R.drawable.snow)
            } else if (state == "Clouds") {
                imgWeatherState.setImageResource(R.drawable.cloudy)
            } else if (state == "Rain") {
                imgWeatherState.setImageResource(R.drawable.rain)
            }

            tvWeatherTemp.text = "$temp°C"

            if (temp.toDouble() >= 28) {
                imgToday.setImageResource(R.drawable.do28)
            }else if(temp.toDouble() >= 23){
                imgToday.setImageResource(R.drawable.do27)
            }else if(temp.toDouble() >= 20){
                imgToday.setImageResource(R.drawable.do22)
            }else if(temp.toDouble() >=  17){
                imgToday.setImageResource(R.drawable.do19)
            }else if(temp.toDouble() >= 12){
                imgToday.setImageResource(R.drawable.do16)
            }else if(temp.toDouble() >= 9){
                imgToday.setImageResource(R.drawable.do11)
            }else if(temp.toDouble() >= 5){
                imgToday.setImageResource(R.drawable.do8)
            }else{
                imgToday.setImageResource(R.drawable.do4)
            }

        }

        return view

    }

}
//5차 수정
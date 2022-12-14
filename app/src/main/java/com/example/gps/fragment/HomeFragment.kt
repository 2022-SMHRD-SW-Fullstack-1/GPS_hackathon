package com.example.gps.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gps.R
import com.example.gps.weather.WeatherAdapter
import com.example.gps.weather.WeatherVO
import org.json.JSONObject

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)


        val imgWeatherState = view.findViewById<ImageView>(R.id.imgWeatherState)
        val rvWeather = view.findViewById<RecyclerView>(R.id.rvWeather)
        val tvCity = view.findViewById<TextView>(R.id.tvCity)
        val tvState = view.findViewById<TextView>(R.id.tvState)

        val weatherList = ArrayList<WeatherVO>()

        //Volley 네트워크 통신 4단계
        //1. Volley 라이브러리 설정
        //1-1. 라이브러리 추가, 인터넷 권한, 요청 방식이 http 인지 https인지 !
        //1-2. http면 manifests에 android:usesCleartextTraffic="true" 추가 하기!
        //2. RequestQueue 생성
        val requestQueue = Volley.newRequestQueue(requireContext())

        val adapter = WeatherAdapter(requireContext(), weatherList)

        rvWeather.adapter = adapter
        rvWeather.layoutManager = LinearLayoutManager(requireContext())


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

//        imgWeather.setOnClickListener {
//            if(weatherList.get(0).state == "Clear"){
//                imgWeather.setImageResource(R.drawable.sunny)
//            }else if(weatherList.get(0).state == "Snow"){
//                imgWeather.setImageResource(R.drawable.snow)
//            }else if (weatherList.get(0).state == "Clouds"){
//                imgWeather.setImageResource(R.drawable.cloudy)
//            }else if(weatherList.get(0).state == "Rain"){
//                imgWeather.setImageResource(R.drawable.rain)
//            }
//            tvWeather.text = weatherList.get(0).temp
//        }



        return view

    }

}
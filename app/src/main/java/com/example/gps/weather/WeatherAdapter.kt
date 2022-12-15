package com.example.gps.weather

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gps.R

class WeatherAdapter(val context: Context, val weatherList: ArrayList<WeatherVO>): RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvCity: TextView
        val tvState: TextView
        val tvWeatherTemp: TextView
         init {
             tvCity = itemView.findViewById(R.id.tvCity)
             tvState = itemView.findViewById(R.id.tvState)
             tvWeatherTemp = itemView.findViewById(R.id.tvWeatherTemp)
         }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.weather_list, null)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvCity.text = weatherList[position].city
        holder.tvState.text = weatherList[position].state
        holder.tvWeatherTemp.text = weatherList[position].temp
    }

    override fun getItemCount(): Int {

        return weatherList.size
    }

}
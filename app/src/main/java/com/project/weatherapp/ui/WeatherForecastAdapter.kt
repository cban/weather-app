package com.project.weatherapp.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.weatherapp.R
import com.project.weatherapp.data.DailyWeather
import com.project.weatherapp.databinding.WeatherItemBinding
import com.project.weatherapp.utils.loadFromUrl
import javax.inject.Inject

class WeatherForecastAdapter @Inject constructor(
    private val onItemClicked: (item: DailyWeather) -> Unit
) : ListAdapter<DailyWeather, WeatherForecastAdapter.ViewHolder>(DIFF_CALLBACKS) {
    private lateinit var binding: WeatherItemBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeatherForecastAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = WeatherItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding.root)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(dailyWeather: DailyWeather) {
            val degrees = itemView.context.getString(R.string.degrees_celcius)
            val icon: String = dailyWeather.icon
            val iconUrl = itemView.context.getString(R.string.icon_url, icon)
            binding.dayText.text = dailyWeather.day
            binding.temperatureText.text = "${dailyWeather.temp.toInt()} $degrees"
            binding.weatherIcon.loadFromUrl(iconUrl)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClicked(getItem(holder.adapterPosition))
        }
    }

    companion object {
        val DIFF_CALLBACKS = object : DiffUtil.ItemCallback<DailyWeather>() {
            override fun areItemsTheSame(
                oldItem: DailyWeather,
                newItem: DailyWeather
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DailyWeather,
                newItem: DailyWeather
            ): Boolean {
                return true
            }
        }
    }
}
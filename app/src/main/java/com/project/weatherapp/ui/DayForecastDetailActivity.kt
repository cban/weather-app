package com.project.weatherapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.weatherapp.R
import com.project.weatherapp.data.DailyWeather
import com.project.weatherapp.databinding.ActivityDayForecastDetailBinding
import com.project.weatherapp.ui.MainActivity.Companion.DAY_DETAILS_ID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DayForecastDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDayForecastDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDayForecastDetailBinding.inflate(layoutInflater)
        val dailyWeather: DailyWeather? = intent.getParcelableExtra(DAY_DETAILS_ID)
        setContentView(binding.root)
        dailyWeather?.let { setupViews(it) }
    }

    private fun setupViews(dailyWeather: DailyWeather) {
        val degrees = getString(R.string.degrees_celcius)
        binding.dayText.text = dailyWeather.day
        binding.detailsDegrees.text = dailyWeather.temp.toInt().toString()
        binding.weatherDescription.text = dailyWeather.description
        binding.wind.text =  getString(R.string.speed,dailyWeather.wind)
        binding.tempMax.text =  "${dailyWeather.temp_max.toInt()} $degrees"
        binding.tempMin.text = "${dailyWeather.temp_min.toInt()} $degrees"
        binding.humidity.text = getString(R.string.percentage,dailyWeather.humidity)
        binding.pressure.text = dailyWeather.pressure
        binding.seaLevel.text = dailyWeather.sea_level
        binding.mainDetailLayout.setBackgroundResource(R.drawable.clear_back)
    }
}
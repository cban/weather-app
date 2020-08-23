package com.project.weatherapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.weatherapp.R
import com.project.weatherapp.data.DetailedDayWeather
import com.project.weatherapp.databinding.ActivityDayForecastDetailBinding
import com.project.weatherapp.ui.MainActivity.Companion.DAY_DETAILS_ID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DayForecastDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDayForecastDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDayForecastDetailBinding.inflate(layoutInflater)
        val detailedDayWeather: DetailedDayWeather? = intent.getParcelableExtra(DAY_DETAILS_ID)
        setContentView(binding.root)
        detailedDayWeather?.let { setupUI(it) }
    }

    private fun setupUI(detailedDayWeather: DetailedDayWeather) {
        val degrees = getString(R.string.degrees_celcius)
        binding.dayText.text = detailedDayWeather.day
        binding.detailsDegrees.text = detailedDayWeather.temp
        binding.weatherDescription.text = detailedDayWeather.description
        binding.wind.text =  getString(R.string.speed,detailedDayWeather.wind)
        binding.tempMax.text =  "${detailedDayWeather.temp_max} $degrees"
        binding.tempMin.text = "${detailedDayWeather.temp_min} $degrees"
        binding.humidity.text = getString(R.string.percentage,detailedDayWeather.humidity)
        binding.pressure.text = detailedDayWeather.pressure
        binding.seaLevel.text = detailedDayWeather.sea_level
        binding.mainDetailLayout.setBackgroundResource(R.drawable.clear_back)
    }
}
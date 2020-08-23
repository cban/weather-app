package com.project.weatherapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
        detailedDayWeather?.let { updateUi(it) }
        setContentView(binding.root)
    }

    private fun updateUi(detailedDayWeather: DetailedDayWeather) {
        binding.wind.text = detailedDayWeather.wind
        binding.tempMax.text = detailedDayWeather.temp_max
        binding.tempMin.text = detailedDayWeather.temp_min
        binding.humidity.text = detailedDayWeather.humidity
        binding.pressure.text = detailedDayWeather.pressure
    }
}

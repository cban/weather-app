package com.project.weatherapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.project.weatherapp.R
import com.project.weatherapp.data.DetailedDayWeather
import com.project.weatherapp.data.WeatherResponse
import com.project.weatherapp.databinding.ActivityMainBinding
import com.project.weatherapp.utils.Status
import com.project.weatherapp.utils.hide
import com.project.weatherapp.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: WeatherForecastViewModel by viewModels()
    private lateinit var adapter: WeatherForecastAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        adapter = WeatherForecastAdapter { dailyDetailedWeather ->

            launchDetailsScreen(dailyDetailedWeather)
        }
        binding.recyclerView.adapter = adapter
        setLocation()
        setContentView(binding.root)
    }

    private fun launchDetailsScreen(detailedDayWeather: DetailedDayWeather) {
        val intent = Intent(this, DayForecastDetailActivity::class.java)
        intent.putExtra(DAY_DETAILS_ID, detailedDayWeather)
        startActivity(intent)
    }

    private fun animation(weatherResponse: WeatherResponse?) {
        when (weatherResponse?.list?.get(0)?.weather?.get(0)?.main) {
            "Clear" -> {
                binding.imgHeader.setAnimation(R.raw.weather_sunny)
                binding.mainLayout.setBackgroundResource(R.drawable.dark_background)
            }
            "Clouds" -> {
                binding.imgHeader.setAnimation(R.raw.weather_cloudy)
                binding.mainLayout.setBackgroundResource(R.drawable.dark_background)
            }
            "Rain", "Drizzle", "Thunderstorm" -> {
                binding.imgHeader.setAnimation(R.raw.weather_rain)
                binding.mainLayout.setBackgroundResource(R.drawable.dark_background)
            }
            "Snow" -> {
                binding.imgHeader.setAnimation(R.raw.weather_snow)
                binding.mainLayout.setBackgroundResource(R.drawable.dark_background)
            }
            else -> {
                binding.imgHeader.setAnimation(R.raw.weather_cloudy)
                binding.mainLayout.setBackgroundResource(R.drawable.dark_background)
            }
        }
        binding.imgHeader.show()
    }

    private fun updateUi(weatherResponse: WeatherResponse?) {
        binding.city.text = weatherResponse?.city?.name
        binding.degrees.text = weatherResponse?.list?.get(0)?.main?.temp.toString()
        binding.weatherCondition.text = weatherResponse?.list?.get(0)?.weather?.get(0)?.main

    }


    private fun setLocation() {
        viewModel.getWeatherForeCastDataByLocation("-26.2023", "28.0436", "metric")
        viewModel.weatherResponse.observe(this, Observer { weatherResponse ->
            when (weatherResponse.status) {
                Status.SUCCESS -> {
                    animation(weatherResponse.data)
                    updateUi(weatherResponse.data)
                    binding.animationView.hide()
                    viewModel.dailyWeatherForecastList.observe(this, Observer {
                        adapter.submitList(it)
                    })
                }
                Status.ERROR -> {
                    binding.animationView.hide()
                }
                Status.LOADING -> {
                    binding.animationView.show()
                }
            }
        })
    }

    companion object {
        const val DAY_DETAILS_ID = "id"
    }
}
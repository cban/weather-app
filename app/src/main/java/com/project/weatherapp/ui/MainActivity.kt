package com.project.weatherapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.project.weatherapp.data.DetailedDayWeather
import com.project.weatherapp.databinding.ActivityMainBinding
import com.project.weatherapp.utils.Status
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

    private fun setLocation() {
        viewModel.getWeatherForeCastDataByLocation("-26.2023", "28.0436", "metric")
        viewModel.weatherResponse.observe(this, Observer { it ->
            when (it.status) {
                Status.SUCCESS -> {
                    viewModel.dailyWeatherForecastList.observe(this, Observer {
                        adapter.submitList(it)
                        Toast.makeText(this, "SUCCESS", Toast.LENGTH_LONG).show()
                    })
                }
                Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
                Status.LOADING -> {
                    Toast.makeText(this, "LOADING", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    companion object {
        const val DAY_DETAILS_ID = "id"
    }
}
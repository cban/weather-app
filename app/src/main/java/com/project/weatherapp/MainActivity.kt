package com.project.weatherapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.project.weatherapp.databinding.ActivityMainBinding
import com.project.weatherapp.ui.WeatherForecastAdapter
import com.project.weatherapp.ui.WeatherForecastViewModel
import com.project.weatherapp.utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel : WeatherForecastViewModel by viewModels()
    private lateinit var adapter: WeatherForecastAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        adapter = WeatherForecastAdapter { _ ->
            TODO()
        }
        binding.recyclerView.adapter = adapter
        setLocation()
        setContentView(binding.root)
    }

    private fun setLocation() {
        viewModel.getWeatherForeCastDataByLocation("-26.2023", "28.0436", "metric")
        viewModel.weatherResponse.observe(this, Observer { it ->
            when (it.status) {
                Status.SUCCESS -> {
                    Toast.makeText(this,"WORKES",Toast.LENGTH_LONG).show()
                    viewModel.dailyWeatherForecastList.observe(this, Observer {
                        adapter.submitList(it)
                    })
                }
                Status.ERROR -> {
                    Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
                    Log.d("MAIN",it.message.toString())
                    Log.d("MAIN",it.data.toString())
                }
                Status.LOADING -> {
                    Toast.makeText(this,"LOADING",Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}
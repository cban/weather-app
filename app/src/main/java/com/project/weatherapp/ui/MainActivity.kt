package com.project.weatherapp.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.google.android.gms.location.*
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
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val viewModel: WeatherForecastViewModel by viewModels()
    private lateinit var adapter: WeatherForecastAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        adapter = WeatherForecastAdapter { dailyDetailedWeather ->
            launchDetailsScreen(dailyDetailedWeather)

        }
        binding.recyclerView.adapter = adapter
        getLocation()
        setContentView(binding.root)
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                location?.let { setLocation(it.latitude.toString(), it.longitude.toString()) }
            }
            binding.animationView.show()
        }
    }

    fun isOnline(): Boolean {
        val connMgr =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), REQUEST_LOCATION_PERMISSION
            )
            return
        } else {
            fusedLocationClient.requestLocationUpdates(
                getLocationRequest(), mLocationCallback,
                null /* Looper */
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION ->
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    getLocation()
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.permissions_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun getLocationRequest(): LocationRequest {
        val locationRequest = LocationRequest()
        locationRequest.interval = UPDATES_INTERVAL
        locationRequest.fastestInterval = FASTEST_UPDATE_INTERVAL
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        return locationRequest
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
            SNOW -> {
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
        binding.mainLayout.show()
    }

    private fun setLocation(latitude: String, longitude: String) {
        viewModel.getWeatherForeCastDataByLocation(latitude, longitude, "metric")
        viewModel.weatherResponse.observe(this, Observer { weatherResponse ->
            when (weatherResponse.status) {
                Status.SUCCESS -> {
                    binding.animationView.hide()
                    animation(weatherResponse.data)
                    updateUi(weatherResponse.data)
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
        private const val REQUEST_LOCATION_PERMISSION = 100
        private const val UPDATES_INTERVAL = 1000 * 60.toLong()
        private const val FASTEST_UPDATE_INTERVAL = 1000 * 30.toLong()
        private const val SNOW ="Snow"
    }
}
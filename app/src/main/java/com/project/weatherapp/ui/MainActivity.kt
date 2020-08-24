package com.project.weatherapp.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.google.android.gms.location.*
import com.project.weatherapp.R
import com.project.weatherapp.data.DailyWeather
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
        setContentView(binding.root)
        adapter = WeatherForecastAdapter { dailyDetailedWeather ->
            launchDetailsScreen(dailyDetailedWeather)
        }

        binding.recyclerView.adapter = adapter
        binding.animationView.hide()
        binding.weatherAnimation.playAnimation()
        init()
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

    private fun launchDetailsScreen(dailyWeather: DailyWeather) {
        val intent = Intent(this, DayForecastDetailActivity::class.java)
        intent.putExtra(DAY_DETAILS_ID, dailyWeather)
        startActivity(intent)
    }

    private fun init() {
        if (isOnline()) {
            getLocation()
        } else {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(getString(R.string.internet_connection_message))
                .setTitle(getString(R.string.internet_connection_title))
            builder.setNeutralButton(
                getString(R.string.ok_title)
            ) { dialog, _ ->
                val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(intent)
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    private fun setupHeaderUI(weatherResponse: WeatherResponse?) {
        when (weatherResponse?.list?.get(0)?.weather?.get(0)?.main) {
            CLEAR -> {
                binding.weatherAnimation.setAnimation(R.raw.weather_sunny)
                binding.mainLayout.setBackgroundResource(R.drawable.dark_background)
            }
            CLOUDS -> {
                binding.weatherAnimation.setAnimation(R.raw.weather_cloudy)
                binding.mainLayout.setBackgroundResource(R.drawable.dark_background)
            }
            RAIN, DRIZZLE, THUNDERSTORM -> {
                binding.weatherAnimation.setAnimation(R.raw.weather_rain)
                binding.mainLayout.setBackgroundResource(R.drawable.dark_background)
            }
            SNOW -> {
                binding.weatherAnimation.setAnimation(R.raw.weather_snow)
                binding.mainLayout.setBackgroundResource(R.drawable.dark_background)
            }
            else -> {
                binding.weatherAnimation.setAnimation(R.raw.weather_cloudy)
                binding.mainLayout.setBackgroundResource(R.drawable.dark_background)
            }
        }
        binding.weatherAnimation.show()
    }

    private fun updateViews(weatherResponse: WeatherResponse?) {
        binding.city.text = weatherResponse?.city?.name
        binding.degrees.text = weatherResponse?.list?.get(0)?.main?.temp?.toInt().toString()
        binding.weatherCondition.text = weatherResponse?.list?.get(0)?.weather?.get(0)?.main
        binding.mainLayout.show()
    }

    private fun setLocation(latitude: String, longitude: String) {
        viewModel.getWeatherForeCastDataByLocation(latitude, longitude, "metric")
        viewModel.weatherResponse.observe(this, Observer { weatherResponse ->
            when (weatherResponse.status) {
                Status.SUCCESS -> {
                    binding.animationView.pauseAnimation()
                    binding.animationView.hide()
                    setupHeaderUI(weatherResponse.data)
                    updateViews(weatherResponse.data)
                    viewModel.dailyWeatherForecastList.observe(this, Observer {
                        adapter.submitList(it)
                    })
                }
                Status.ERROR -> {
                    binding.animationView.hide()
                    binding.animationView.pauseAnimation()
                    Toast.makeText(
                        this,
                        getString(R.string.load_error_message),
                        Toast.LENGTH_LONG
                    ).show()
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
        private const val SNOW = "Snow"
        private const val CLEAR = "Clear"
        private const val CLOUDS = "Clouds"
        private const val RAIN = "Rain"
        private const val DRIZZLE = "Drizzle"
        private const val THUNDERSTORM = "Thunderstorm"
    }
}
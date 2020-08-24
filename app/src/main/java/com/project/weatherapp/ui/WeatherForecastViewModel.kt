package com.project.weatherapp.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.weatherapp.data.DailyWeather
import com.project.weatherapp.data.WeatherList
import com.project.weatherapp.data.WeatherResponse
import com.project.weatherapp.repository.WeatherRepository
import com.project.weatherapp.utils.Resource
import com.project.weatherapp.utils.formatWeek
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherForecastViewModel @ViewModelInject constructor(private val repository: WeatherRepository) :
    ViewModel() {

    private var _weatherResponse = MutableLiveData<Resource<WeatherResponse>>()
    val weatherResponse: MutableLiveData<Resource<WeatherResponse>>
        get() = _weatherResponse

    private var _dailyWeatherForecastList = MutableLiveData<List<DailyWeather>>()
    val dailyWeatherForecastList: LiveData<List<DailyWeather>>
        get() = _dailyWeatherForecastList

    private fun setWeatherForecastList(response: WeatherResponse) {
        val weatherList: List<WeatherList> = response.list
        val dailyWeatherList: MutableList<DailyWeather> = arrayListOf()
        val filteredWeatherList = weatherList.filter { it.dt_txt.endsWith("12:00:00") }

        filteredWeatherList.forEach { weatherListItem ->
            val day: String = formatWeek(weatherListItem.dt_txt)
            val dailyWeather =
                DailyWeather(
                    day,
                    weatherListItem.main.temp,
                    weatherListItem.main.temp_min,
                    weatherListItem.main.temp_max,
                    weatherListItem.main.pressure.toString(),
                    weatherListItem.main.sea_level.toString(),
                    weatherListItem.main.humidity.toString(),
                    weatherListItem.wind.speed.toString(),
                    weatherListItem.weather[0].icon,
                    weatherListItem.weather[0].description
                )
            dailyWeatherList.add(dailyWeather)
        }
        _dailyWeatherForecastList.postValue(dailyWeatherList)
    }

    fun getWeatherForeCastDataByLocation(latitude: String, longitude: String, units: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _weatherResponse.postValue(Resource.loading(null))
            repository.getWeatherForecast(latitude, longitude, units).let { response ->
                if (response.isSuccessful) {
                    response.body()?.let { setWeatherForecastList(it) }
                    _weatherResponse.postValue(Resource.success(response.body()))
                } else _weatherResponse.postValue(
                    Resource.error(
                        response.errorBody().toString() + response.raw().body,
                        null
                    )
                )
            }
        }
    }
}
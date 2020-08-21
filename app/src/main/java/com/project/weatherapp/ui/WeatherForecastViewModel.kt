package com.project.weatherapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potter.triwizard.util.Resource
import com.project.weatherapp.data.WeatherResponse
import com.project.weatherapp.repository.WeatherRepository
import kotlinx.coroutines.launch

class WeatherForecastViewModel(private val repository: WeatherRepository) : ViewModel() {

    private var _weatherResponse = MutableLiveData<Resource<WeatherResponse>>()
    val weatherResponse: MutableLiveData<Resource<WeatherResponse>>
        get() = _weatherResponse

    fun getWeatherForeCastData(latitude: String, longitude: String, units: String) {
        viewModelScope.launch {
            _weatherResponse.postValue(Resource.loading(null))
            repository.getWeatherForecast(latitude, longitude, units).let { response ->
                if (response.isSuccessful) {
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
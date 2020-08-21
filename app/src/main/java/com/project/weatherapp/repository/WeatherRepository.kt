package com.project.weatherapp.repository

import com.project.weatherapp.data.WeatherResponse
import retrofit2.Response

interface WeatherRepository {
   suspend fun getWeatherForecast(
        latitude: String,
        longitude: String,
        units: String
    ): Response<WeatherResponse>
}
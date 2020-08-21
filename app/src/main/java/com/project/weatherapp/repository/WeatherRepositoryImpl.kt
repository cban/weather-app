package com.project.weatherapp.repository

import com.project.weatherapp.data.WeatherResponse
import com.project.weatherapp.network.WeatherApi
import retrofit2.Response

class WeatherRepositoryImpl(private val weatherApiService: WeatherApi) : WeatherRepository {
    override suspend fun getWeatherForecast(
        latitude: String,
        longitude: String,
        units: String
    ): Response<WeatherResponse> {
        return weatherApiService.getDailyForecast(latitude, longitude, units)
    }
}
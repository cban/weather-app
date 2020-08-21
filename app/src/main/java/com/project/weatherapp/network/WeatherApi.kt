package com.project.weatherapp.network

import com.project.weatherapp.data.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherApi {

    @GET("weather")
    suspend fun getDailyForecast(
        @Query("lat") latitude: String?,
        @Query("lon") longitude: String?,
        @Query("units") units: String?
    ): Response<WeatherResponse>
}
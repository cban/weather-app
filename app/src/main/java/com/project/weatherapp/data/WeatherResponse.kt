package com.project.weatherapp.data

data class WeatherResponse(
    val cod: Int,
    val message: Double,
    val cnt: Int,
    val list: List<WeatherList>,
    val city: City
)

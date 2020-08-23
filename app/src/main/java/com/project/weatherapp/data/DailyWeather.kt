package com.project.weatherapp.data

data class DailyWeather(
    var dt:Int,
    var day: String,
    val temperature: String,
    val icon: String
)
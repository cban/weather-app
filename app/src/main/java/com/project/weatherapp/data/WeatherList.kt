package com.project.weatherapp.data

data class WeatherList(
    val dt: Int,
    val main: Main,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val sys: Sys,
    val dt_txt: String
)

data class Sys(
    val pod: String
)

data class Main(
    val temp: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Double,
    val sea_level: Double,
    val grnd_level: Double,
    val humidity: Int,
    val temp_kf: Double
)

data class Clouds(
    val all: Int
)

data class Wind(
    val speed: Double,
    val deg: Double
)
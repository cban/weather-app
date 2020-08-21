package com.project.weatherapp.data

data class City(
    val id: Int,
    val name: String,
    val coord: Coord,
    val country: String
)

data class Coord(
    val lat: Double,
    val lon: Double
)
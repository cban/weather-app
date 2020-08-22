package com.project.weatherapp.data

data class City(
    val id: Int,
    val name: String,
    val coord: Coord,
    val country: String,
    val population : Int,
    val timezone : Int,
    val sunrise : Int,
    val sunset : Int
)

data class Coord(
    val lat: Double,
    val lon: Double
)
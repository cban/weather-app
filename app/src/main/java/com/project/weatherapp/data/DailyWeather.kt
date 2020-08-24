package com.project.weatherapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DailyWeather(
    val day: String,
    val temp: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: String,
    val sea_level: String,
    val humidity: String,
    val wind: String,
    val icon: String,
    val description: String
) : Parcelable
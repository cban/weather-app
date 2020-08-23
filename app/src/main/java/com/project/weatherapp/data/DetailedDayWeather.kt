package com.project.weatherapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DetailedDayWeather(
    val day:String,
    val temp: String,
    val temp_min: String,
    val temp_max: String,
    val pressure: String,
    val sea_level: String,
    val humidity: String,
    val wind:String
) : Parcelable
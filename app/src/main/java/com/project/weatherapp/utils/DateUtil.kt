package com.project.weatherapp.utils

import java.text.SimpleDateFormat
import java.util.*


fun Date.formatDate(pattern: String): String {
    return SimpleDateFormat(pattern, Locale.getDefault()).format(this)
}

fun formatWeek(date: String): String {
    val weekdayDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(date)
    return weekdayDate.formatDate("EEEE")
}

fun getCurrentDate(): String {
    return SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss",
        Locale.getDefault()
    ).format(Calendar.getInstance().time)
}



package com.project.weatherapp.ui

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.weatherapp.data.WeatherList
import javax.inject.Inject

class WeatherForecastAdapter @Inject constructor(
    private val onItemClicked: (item: WeatherList) -> Unit
) : ListAdapter<WeatherList, WeatherForecastAdapter.ViewHolder>(DIFF_CALLBACKS) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeatherForecastAdapter.ViewHolder {
        TODO("Not yet implemented")
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    companion object {
        val DIFF_CALLBACKS = object : DiffUtil.ItemCallback<WeatherList>() {
            override fun areItemsTheSame(oldItem: WeatherList, newItem: WeatherList): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: WeatherList, newItem: WeatherList): Boolean {
                return true
            }
        }
    }
}


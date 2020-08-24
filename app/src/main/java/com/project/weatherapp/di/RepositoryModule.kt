package com.project.weatherapp.di

import com.project.weatherapp.repository.WeatherRepository
import com.project.weatherapp.repository.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideWeatherRepository(impl: WeatherRepositoryImpl): WeatherRepository
}

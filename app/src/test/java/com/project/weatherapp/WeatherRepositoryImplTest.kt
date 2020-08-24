package com.project.weatherapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.project.weatherapp.data.WeatherResponse
import com.project.weatherapp.network.WeatherApi
import com.project.weatherapp.repository.WeatherRepositoryImpl
import junit.framework.Assert
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.Response

class WeatherRepositoryImplTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var api: WeatherApi
    private lateinit var repository: WeatherRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = WeatherRepositoryImpl(api)
    }

    @Test
    fun test_weather_repo_retrieves_data() {
        val response = mock<Response<WeatherResponse>>()
        runBlocking {
            whenever(api.getDailyForecast("28","28","metric")).thenReturn(response)
            val dataReceived = repository.getWeatherForecast("28","28","metric")
            MatcherAssert.assertThat(response, IsEqual(dataReceived))
        }
    }
}
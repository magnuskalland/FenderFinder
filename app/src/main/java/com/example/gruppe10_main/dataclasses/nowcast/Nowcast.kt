package com.example.gruppe10_main.dataclasses.nowcast
import com.example.gruppe10_main.dataclasses.nowcast.ForecastDto
import retrofit2.http.GET
import retrofit2.http.Query

interface Nowcast {
    // For setting up the GET request
    @GET("weatherapi/nowcast/2.0/complete")
    suspend fun fetchForecastForLocation(
            @Query("lat") latitude: Double,
            @Query("lon") longitude: Double)
            : ForecastDto
}
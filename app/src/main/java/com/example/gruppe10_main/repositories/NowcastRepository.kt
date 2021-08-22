package com.example.gruppe10_main.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.gruppe10_main.dataclasses.nowcast.ForecastDto
import com.example.gruppe10_main.model.ForecastModel
import com.example.gruppe10_main.dataclasses.nowcast.Nowcast
import com.example.gruppe10_main.misc.NOWCAST_BASE_URL
import com.example.gruppe10_main.misc.TAG
import com.example.gruppe10_main.misc.Utility
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
/**
 * Class to handle API calls with Nowcast https://in2000-apiproxy.ifi.uio.no/weatherapi/nowcast/2.0/
 */
class NowcastRepository {

    val forecastModelLiveData = MutableLiveData<ForecastModel>()

    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(NOWCAST_BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    /**
     * Perform GET request on given [lat] and [lon].
     */
    suspend fun fetchForecast(lat: Double, lon: Double) {
        Log.d(TAG, "Fetching Nowcast forecast at " +
                "${Utility.decimalDegreesToDegreeMinute(lat, true)} " +
                Utility.decimalDegreesToDegreeMinute(lon, false))
        Utility.startTimer()
        val service: Nowcast = retrofit.create(Nowcast::class.java)
        val result: ForecastDto?

        try {
            result = service.fetchForecastForLocation(lat, lon)
        } catch (e: Exception) {
            Log.d(TAG, "Invalid result for coordinates " +
                    "${Utility.decimalDegreesToDegreeMinute(lat, true)} " +
                    Utility.decimalDegreesToDegreeMinute(lon, false))
            return
        }

        // If valid request
        val forecastModel = ForecastModel(
            windSpeed = result.properties.timeseries.first().data.instant.details.wind_speed,
            degrees = result.properties.timeseries.first().data.instant.details.air_temperature,
            windDirection = result.properties.timeseries.first().data.instant.details.wind_from_direction.toInt(),
            time = result.properties.timeseries.first().time,
            precipitationRate = result.properties.timeseries.first().data.instant.details.precipitation_rate,
        )

        forecastModelLiveData.postValue(forecastModel)
        Log.d(TAG, "Finished fetching weather after ${Utility.endTimer()} ms")
        Log.d(TAG, "    $forecastModel")
    }
}


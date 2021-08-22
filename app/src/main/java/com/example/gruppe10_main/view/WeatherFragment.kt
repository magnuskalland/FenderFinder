package com.example.gruppe10_main.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.example.gruppe10_main.R
import com.example.gruppe10_main.model.ForecastModel
import com.example.gruppe10_main.misc.TAG
import com.example.gruppe10_main.viewmodel.NowcastViewModel
import kotlin.math.roundToInt

/**
 * Class for displaying weather info.
 */
class WeatherFragment: ParentFragment(R.layout.fragment_weather) {

    // Weather views
    val viewModel by viewModels<NowcastViewModel>()
    private lateinit var temperatureView: TextView
    private lateinit var windSpeedView: TextView
    private lateinit var precipitationView: TextView

    private lateinit var fragmentView: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.v(TAG, "WeatherFragment onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        fragmentView = view
        temperatureView = view.findViewById(R.id.result_degrees)
        windSpeedView = fragmentView.findViewById(R.id.result_wind_speed)
        precipitationView = fragmentView.findViewById(R.id.result_precipitation)
        initWeatherListener()
    }

    /**
     * Sets [viewModel]s to observe updates in its live data.
     */
    private fun initWeatherListener() {
        Log.d(TAG, "Initializing weather listener")
        viewModel.forecastModelLiveData.observe(viewLifecycleOwner) {
            Log.d(TAG, "Live weather data changed")
            populateViews(it)
        }
    }

    /**
     * Set view values based on [forecastModel] for when weather data updates.
     */
    private fun populateViews(forecastModel: ForecastModel) {
        Log.d(TAG, "MapFragment populate weather view")

        windSpeedView.text = String.format("%.1f %s",
            forecastModel.windSpeed,
            getString(R.string.wind_speed_default))

        temperatureView.text = String.format("%d %s",
            forecastModel.degrees.roundToInt(),
            getString(R.string.temperature_default))

        precipitationView.text = String.format("%.1f %s",
            forecastModel.precipitationRate,
            getString(R.string.precipitation_default))

        setArrowDirection(forecastModel.windDirection)
    }

    /**
     * Rotates this views arrow icon based on [direction].
     */
    private fun setArrowDirection(direction: Int) {
        val iconArrow = fragmentView.findViewById<ImageView>(R.id.icon_arrow)
        iconArrow.rotation = direction.toFloat()
    }
}
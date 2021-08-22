package com.example.gruppe10_main.model
// Data class for representing Nowcast response
data class ForecastModel(
        val windSpeed: Double,
        val degrees: Double,
        val windDirection: Int,
        val time: String,
        val precipitationRate: Double,
)

package com.example.gruppe10_main.dataclasses.nowcast

import com.example.gruppe10_main.dataclasses.nowcast.Geometry
import com.example.gruppe10_main.dataclasses.nowcast.Properties

data class Weather(
        val geometry: Geometry,
        val properties: Properties,
        val type: String
)

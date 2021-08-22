package com.example.gruppe10_main.dataclasses.drifty

import com.example.gruppe10_main.dataclasses.drifty.Configuration
import com.example.gruppe10_main.dataclasses.drifty.Geo

data class Spec(val model: String?,
                val geo: Geo?,
                val simulationDurationInHours: Number?,
                val configuration: Configuration?)

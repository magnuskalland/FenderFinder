package com.example.gruppe10_main.dataclasses

import com.example.gruppe10_main.dataclasses.drifty.Configuration
import com.example.gruppe10_main.dataclasses.drifty.Geo

data class DriftyPostRequestBodyDataClass(val model: String,
                                          val geo: Geo,
                                          val simulationDurationInHours: Number,
                                          val configuration: Configuration
)

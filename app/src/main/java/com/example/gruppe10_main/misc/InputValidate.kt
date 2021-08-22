package com.example.gruppe10_main.misc

interface InputValidate {

    /**
     * Throws an [IllegalArgumentException] if [latitude]
     * is outside of range [MIN_LAT]..[MAX_LAT]
     */
    fun checkLatitude(latitude: Double?)

    /**
     * Throws an [IllegalArgumentException] if [longitude]
     * is outside of range [MIN_LON]..[MAX_LON]
     */
    fun checkLongitude(longitude: Double?)

    /**
     * Throws an [IllegalArgumentException] if [radius]
     * is outside of range [MIN_RADIUS]..[MAX_RADIUS]
     */
    fun checkRadius(radius: Int)

    /**
     * Throws an [IllegalArgumentException] if formatted [date]
     * is outside of range [date] - [MAX_DAYS_IN_PAST]..[date] + [MAX_DAYS_IN_FUTURE]
     */
    fun checkDate(date: String)

    /**
     * Throws an [IllegalArgumentException] if formatted [time]
     * outside of range
     */
    fun checkTime(time: String)

    /**
     * Throws an [IllegalArgumentException] if [hours]
     * is outside of range 0..[MAX_SIMULATION_DURATION]
     */
    fun checkSimulationDuration(hours: Int)
}
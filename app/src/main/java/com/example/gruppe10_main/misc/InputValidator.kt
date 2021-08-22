package com.example.gruppe10_main.misc

import java.lang.NullPointerException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * Class that implements the [InputValidate] interface.
 */
class InputValidator: InputValidate {
    override fun checkLatitude(latitude: Double?) {
        if (latitude == null) throw NullPointerException("Latitude can't be null")
        if (latitude !in MIN_LAT..MAX_LAT)
            throw IllegalArgumentException("Latitude $latitude must be between $MIN_LAT and $MAX_LAT")
    }

    override fun checkLongitude(longitude: Double?) {
        if (longitude == null) throw NullPointerException("Longitude can't be null")
        if (longitude !in MIN_LON..MAX_LON)
            throw IllegalArgumentException("Longitude $longitude must be between $MIN_LON and $MAX_LON")
    }

    override fun checkRadius(radius: Int) {
        if (radius !in MIN_RADIUS..MAX_RADIUS)
            throw IllegalArgumentException("Radius $radius must be between $MIN_RADIUS and $MAX_RADIUS")
    }

    override fun checkDate(date: String) {
        val parsedDate = LocalDate.parse(date)
        if (parsedDate.compareTo(LocalDate.now()) != 0 && !parsedDate.isBefore(LocalDate.now()))
            throw IllegalArgumentException("Date '$parsedDate' " +
                "must be in the range of ten days ago to ten days ahead.")
    }

    override fun checkTime(time: String){
        if (LocalTime.parse(time) !in LocalTime.MIN..LocalTime.MAX)
            throw IllegalArgumentException("Time ${LocalTime.parse(time)} " +
                "must be between ${LocalTime.MIN} and ${LocalTime.MAX}")
    }

    override fun checkSimulationDuration(hours: Int) {
        if (hours < 1|| hours > MAX_SIMULATION_DURATION)
            throw IllegalArgumentException(
                "Simulation duration '$hours hours' must be from 0 (exclusive) " +
                "to $MAX_SIMULATION_DURATION (inclusive). Check date and time.")
    }

    /**
     * Returns true if formatted [date] and [time] is before current time, else false.
     */
    fun timeIsInPast(time: String, date: String) =
        LocalDateTime.parse("${date}T$time").isBefore(LocalDateTime.now())
}
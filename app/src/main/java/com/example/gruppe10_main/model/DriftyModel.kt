package com.example.gruppe10_main.model

import android.util.Log
import com.example.gruppe10_main.R
import com.example.gruppe10_main.misc.*
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * Class for representing runtime input and MutableLiveData for interaction related to Drifty.
 */
class DriftyModel {
    var latitude: Double? = null
    var longitude: Double? = null
    var radius = 0
    var hours = 1 // Can not be 0
    var time = LocalTime.now().toString().substring(0, 5)
    var date = LocalDate.now().toString()
    var objectType: String =
        App.getContext().resources.getStringArray(R.array.leeway_items)[0]

    /**
     * Set method for lost object coordinates. Takes a [inputLat] and [inputLon] and returns
     * this [DriftyModel] to be posted to an instance of this class a MutableLiveData.
     */
    fun setCoordinates(inputLat: Double?, inputLon: Double?): DriftyModel {
        latitude = inputLat
        longitude = inputLon
        return this
    }

    /**
     * Set method for lost object coordinates. Takes a [inputRadius] and returns this
     * [DriftyModel] to be posted to an instance of this class a MutableLiveData.
     */
    fun setRadius(inputRadius: Int): DriftyModel {
        radius = inputRadius
        return this
    }

    /**
     * Set method for lost object coordinates. Takes a [inputTime] and returns this
     * [DriftyModel] to be posted to an instance of this class a MutableLiveData.
     * Sets [hours] calculated from [date] and [inputTime].
     */
    fun setTime(inputTime: String): DriftyModel {
        time = inputTime
        hours = getHoursUntilNow(time, date)
        if (hours == 0) hours = 1
        return this
    }

    /**
     * Set method for lost object coordinates. Takes a [inputDate] and returns this
     * [DriftyModel] to be posted to an instance of this class a MutableLiveData.
     * Sets [hours] calculated from [time] and [inputDate].
     */
    fun setDate(inputDate: String): DriftyModel {
        date = inputDate
        hours = getHoursUntilNow(time, date)
        if (hours == 0) hours = 1
        return this
    }

    /**
     * Set method for lost object coordinates. Takes a [inputObjectType] and returns this
     * [DriftyModel] to be posted to an instance of this class a MutableLiveData.
     */
    fun setObjectType(inputObjectType: String): DriftyModel {
        objectType = inputObjectType
        return this
    }

    /**
     * Returns an [Int] representing hours between formatted [date] and [time].
     */
    private fun getHoursUntilNow(time: String, date: String): Int {
        val from = LocalDateTime.parse("${date}T$time")
        val hours = Duration.between(from, LocalDateTime.now()).toHours().toInt()
        return if (hours == 0) 1 else hours
    }
}
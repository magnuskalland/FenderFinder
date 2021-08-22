package com.example.gruppe10_main.misc

import android.os.Build
import java.util.*
import kotlin.math.abs

/**
 * Utility functions for application.
 */
object Utility {

    private var startTime: Long = 0

    /**
     * Sets [startTime] to current time.
     */
    fun startTimer() { startTime = System.currentTimeMillis() }

    /**
     * Returns time difference between current time and [startTime].
     */
    fun endTimer(): Long = System.currentTimeMillis() - startTime

    /**
     * Returns device API level.
     */
    fun getApiLevel() = Build.VERSION.SDK_INT

    /**
     * Takes a HTTP [header] where key/value pair is separated by one character.
     * Returns the key of [token] if it exists, else null.
     */
    fun searchForKeyInHeader(header: String, token: String): String? {
        if (!header.contains(token)) return null

        val stringTokens = StringTokenizer(header.substring(header.indexOf(token)))
        stringTokens.nextToken(); stringTokens.nextToken()
        return stringTokens.nextToken()
    }

    /**
     * Function for formatting date and time when given [Int] does not start with a zero.
     */
    fun Int.toStringWithZero(): String = if (this < 10) String.format("0%s", this) else "$this"

    /**
     * Function for converting from decimal coordinate to DMS coordinate. Takes a [coordinate] and
     * a boolean value [latitude] where true means latitude and false mean longitude.
     */
    fun decimalDegreesToDegreeMinute(coordinate: Double, latitude: Boolean): String {
        val degrees = abs(coordinate).toInt()
        val minutes = getRestTime(abs(coordinate), degrees).toInt()
        val seconds = ((getRestTime(abs(coordinate), degrees) - minutes) * 60).round(3)

        val quadrant = if (latitude && coordinate < 0) "S"
            else if (latitude) "N"
            else if (coordinate < 0) "W"
            else "E"

        return "$degrees° $minutes' $seconds\" $quadrant"
    }

    private fun getRestTime(coordinate: Double, degrees: Int) = (coordinate - degrees) * 60

    private fun Double.round(decimals: Int): Double =
            "%.${decimals}f".format(this).replace(",", ".").toDouble()



}
package com.example.gruppe10_main

import com.example.gruppe10_main.misc.*
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException
import java.time.LocalDate
import java.time.LocalTime

class InputValidateTest {
    private val v = InputValidator()

    @Test
    fun test_checkLatitude() {
        v.checkLatitude(-45.38721)
        v.checkLatitude(12.94747)
        v.checkLatitude(-27.45620)
        v.checkLatitude(-40.45426)
        v.checkLatitude(MIN_LAT)
        v.checkLatitude(MAX_LAT)
        v.checkLatitude(0.0)
        v.checkLatitude(45.0)
        v.checkLatitude(-45.0)
        v.checkLatitude(90.0)
        v.checkLatitude(-90.0)
    }

    @Test
    fun test_CheckLatitudeException() {
        assertThrows(IllegalArgumentException::class.java) {
            v.checkLatitude(MIN_LAT - 1)
            v.checkLatitude(MAX_LAT + 1)
            v.checkLatitude(null)
            v.checkLatitude(-91.0)
            v.checkLatitude(91.0)
            v.checkLatitude(-90.0)
       }
    }

    @Test
    fun test_checkLongitude() {
        v.checkLongitude(53.71635)
        v.checkLongitude(-167.38988)
        v.checkLongitude(106.55820)
        v.checkLongitude(-110.43528)
        v.checkLongitude(MIN_LON)
        v.checkLongitude(MAX_LON)
        v.checkLongitude(0.0)
        v.checkLongitude(90.0)
        v.checkLongitude(-90.0)
        v.checkLongitude(180.0)
        v.checkLongitude(-180.0)
    }

    @Test
    fun test_CheckLongitudeException() {
        assertThrows(IllegalArgumentException::class.java) {
            v.checkLongitude(MIN_LON - 1)
            v.checkLongitude(MAX_LON + 1)
            v.checkLongitude(null)
            v.checkLongitude(-181.0)
            v.checkLongitude(181.0)
        }
    }

    @Test
    fun test_Radius() {
        v.checkRadius(MIN_RADIUS)
        v.checkRadius(MAX_RADIUS)
        v.checkRadius(0)
        v.checkRadius(10)
        v.checkRadius(20)
        v.checkRadius(50)
        v.checkRadius(100)
    }

    @Test
    fun test_RadiusException() {
        assertThrows(IllegalArgumentException::class.java) {
            v.checkRadius(MIN_RADIUS - 1)
            v.checkRadius(MAX_RADIUS + 1)
            v.checkRadius(-1)
            v.checkRadius(1000)
        }
    }

    @Test
    fun test_Time() {
        v.checkTime(LocalTime.now().toString())
        v.checkTime("00:00:00")
        v.checkTime("00:00:00")
    }

    @Test
    fun test_TimeException() {
        assertThrows(Exception::class.java) {
            v.checkTime("")
            v.checkTime("This is invalid time")
            v.checkTime(LocalTime.now().toString())
            v.checkTime("This is invalid time")
            v.checkTime("-01:00:00")
            v.checkTime("00:00:00")
            v.checkTime(LocalTime.now().toString())
        }
    }
}
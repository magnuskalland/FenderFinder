package com.example.gruppe10_main

import com.example.gruppe10_main.misc.Utility
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class DegreesStringAlgorithmTest {

    @Test
    fun assert_SampleLatitude_Match_AlgorithmLatitude() {
        assertTrue(Utility.decimalDegreesToDegreeMinute(56.37647, true) == "56° 22' 35.292\" N")
        assertTrue(Utility.decimalDegreesToDegreeMinute(28.01229, true) == "28° 0' 44.244\" N")
        assertTrue(Utility.decimalDegreesToDegreeMinute(41.78642, true) == "41° 47' 11.112\" N")

        assertTrue(Utility.decimalDegreesToDegreeMinute(59.36592, true) == "59° 21' 57.312\" N")
        assertTrue(Utility.decimalDegreesToDegreeMinute(22.93659, true) == "22° 56' 11.724\" N")
        assertTrue(Utility.decimalDegreesToDegreeMinute(46.20907, true) == "46° 12' 32.652\" N")

        assertTrue(Utility.decimalDegreesToDegreeMinute(-20.50567, true) == "20° 30' 20.412\" S")
        assertTrue(Utility.decimalDegreesToDegreeMinute(-26.09778, true) == "26° 5' 52.008\" S")
        assertTrue(Utility.decimalDegreesToDegreeMinute(-70.32973, true) == "70° 19' 47.028\" S")

        assertTrue(Utility.decimalDegreesToDegreeMinute(-19.8457, true) == "19° 50' 44.52\" S")
        assertTrue(Utility.decimalDegreesToDegreeMinute(-34.14504, true) == "34° 8' 42.144\" S")
        assertTrue(Utility.decimalDegreesToDegreeMinute(-44.48844, true) == "44° 29' 18.384\" S")

        assertTrue(Utility.decimalDegreesToDegreeMinute(0.0, true) == "0° 0' 0.0\" N")
        assertTrue(Utility.decimalDegreesToDegreeMinute(90.0, true) == "90° 0' 0.0\" N")
        assertTrue(Utility.decimalDegreesToDegreeMinute(-90.0, true) == "90° 0' 0.0\" S")
    }

    @Test
    fun assert_SampleLongitude_Match_AlgorithmLongitude() {

        assertTrue(Utility.decimalDegreesToDegreeMinute(-137.76999, false) == "137° 46' 11.964\" W")
        assertTrue(Utility.decimalDegreesToDegreeMinute(-86.26608, false) == "86° 15' 57.888\" W")
        assertTrue(Utility.decimalDegreesToDegreeMinute(-67.98483, false) == "67° 59' 5.388\" W")

        assertTrue(Utility.decimalDegreesToDegreeMinute(3.83631, false) == "3° 50' 10.716\" E")
        assertTrue(Utility.decimalDegreesToDegreeMinute(63.9535, false) == "63° 57' 12.6\" E")
        assertTrue(Utility.decimalDegreesToDegreeMinute(139.71522, false) == "139° 42' 54.792\" E")

        assertTrue(Utility.decimalDegreesToDegreeMinute(-72.62074, false) == "72° 37' 14.664\" W")
        assertTrue(Utility.decimalDegreesToDegreeMinute( -46.96446, false) == "46° 57' 52.056\" W")
        assertTrue(Utility.decimalDegreesToDegreeMinute( -12.15587, false) == "12° 9' 21.132\" W")

        assertTrue(Utility.decimalDegreesToDegreeMinute( 38.473, false) == "38° 28' 22.8\" E")
        assertTrue(Utility.decimalDegreesToDegreeMinute( 129.87147, false) == "129° 52' 17.292\" E")
        assertTrue(Utility.decimalDegreesToDegreeMinute( 173.82068, false) == "173° 49' 14.448\" E")

        assertTrue(Utility.decimalDegreesToDegreeMinute( 0.0, false) == "0° 0' 0.0\" E")
        assertTrue(Utility.decimalDegreesToDegreeMinute( 180.0, false) == "180° 0' 0.0\" E")
        assertTrue(Utility.decimalDegreesToDegreeMinute( -180.0, false) == "180° 0' 0.0\" W")
    }
}
package com.example.gruppe10_main.misc

/**
 * All number and string constants for application.
 * String resources are located in resources folders.
 */

// Logcat tag
const val TAG = "FenderFinder"

// How much of a latitude/longitude user must move before calling weather API
const val MIN_COORDINATE_MOVEMENT_FACTOR = 0.00001

// Drifty URLs
const val DRIFTY_BASE_URL = "https://in2000.drifty.met.no"
const val DRIFTY_POST_REQUEST_EXTENSION = "/api/leeway/v1"

// Mapbox URI
const val STYLE_URI = "mapbox://styles/in2000/ckmhp21cl3dxi18np10tug7ky"

// Nowcast URL
const val NOWCAST_BASE_URL = "https://in2000-apiproxy.ifi.uio.no/"

// For handling regular polls to Drifty
const val POLL_INTERVAL_SEC: Long = 10
const val M_MILLIS = 1000
const val MAX_WAITING_TIME_MILLIS = 500000

// Map and tracking
const val DEFAULT_LATITUDE = 63.401114
const val DEFAULT_LONGITUDE = 10.902827
const val ZOOM_LEVEL_HIGH = 10.0
const val ZOOM_LEVEL_LOW = 3.0
const val MIN_TILT = 0.0
const val DEFAULT_INTERVAL_MILLIS: Long = 1000L
const val DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_MILLIS * 5
const val MOVE_CAMERA_DURATION = 1500
const val ZOOM_CAMERA_DURATION = 500
const val ZOOM_INCREMENT = 1.0
const val WATER = "water"

// Icons
const val LOST_OBJECT_ICON = "object_marker_icon"
const val STRANDED_ICON = "stranded_icon"
const val ACTIVE_ICON = "active_icon"
const val LOST_OBJECT_ICON_SIZE = 1.5f
const val STRANDED_ICON_SIZE = 0.8f
const val ACTIVE_ICON_SIZE = 0.8f

// Input validation
const val MIN_LAT = -90.0
const val MAX_LAT = 90.0
const val MIN_LON = -180.0
const val MAX_LON = 180.0
const val MIN_RADIUS = 0
const val MAX_RADIUS = 200
const val MAX_DAYS_IN_PAST = 10
const val MAX_SIMULATION_DURATION = MAX_DAYS_IN_PAST * 24
const val MAX_DAYS_IN_FUTURE = 10
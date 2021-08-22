package com.example.gruppe10_main.repositories

import com.example.gruppe10_main.misc.STYLE_URI
import com.mapbox.mapboxsdk.maps.Style

/**
 * Class for doing API call to get [Style] from Mapbox's web service.
 */
class MapboxRepository {
    fun getStyle() = Style.Builder().fromUri(STYLE_URI)
}


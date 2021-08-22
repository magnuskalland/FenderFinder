package com.example.gruppe10_main.model

/**
 * Class for storing [lostObjectMarkerLat] and [lostObjectMarkerLon].
 * Can be removed with some refactoring.
 */
class MapboxModel {
    var lostObjectMarkerLat: Double? = 0.0
    var lostObjectMarkerLon: Double? = 0.0

     /**
     * Set method for lost object coordinates. Takes a [lat] and [lon] and returns this
     * [MapboxModel] to be posted to an instance of this class a MutableLiveData.
    */
    fun setLostObjectPoint(lat: Double?, lon: Double?): MapboxModel {
        lostObjectMarkerLat = lat
        lostObjectMarkerLon = lon
        return this
    }
}
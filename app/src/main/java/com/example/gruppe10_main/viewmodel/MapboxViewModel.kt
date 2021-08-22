package com.example.gruppe10_main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gruppe10_main.model.MapboxModel
import com.example.gruppe10_main.repositories.MapboxRepository

/**
 * Class for handling interaction between view elements and [MapboxModel] and [MapboxRepository].
 */
class MapboxViewModel: ViewModel() {
    private val mapboxModel = MapboxModel()
    private val mapboxRepository = MapboxRepository()
    private val mapboxModelLiveData = MutableLiveData<MapboxModel>()
    fun getStyle() = mapboxRepository.getStyle()
    fun getLiveData() = mapboxModelLiveData
    fun setLostObjectMarker(lat: Double?, lon: Double?) =
        mapboxModelLiveData.postValue(mapboxModel.setLostObjectPoint(lat, lon))
}
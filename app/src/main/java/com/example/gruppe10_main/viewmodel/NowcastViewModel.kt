package com.example.gruppe10_main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.gruppe10_main.misc.TAG
import com.example.gruppe10_main.repositories.NowcastRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NowcastViewModel : ViewModel() {
    private val nowcastRepository = NowcastRepository()
    val forecastModelLiveData = nowcastRepository.forecastModelLiveData
    private var coroutine: Job? = null

    fun fetchForecast(lat: Double, lon: Double) {
        if (coroutine?.isActive == true) coroutine?.cancel()
        coroutine = GlobalScope.launch(Dispatchers.IO) {
            nowcastRepository.fetchForecast(lat, lon)
        }
    }
}
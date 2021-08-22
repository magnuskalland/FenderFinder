package com.example.gruppe10_main.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.gruppe10_main.dataclasses.DriftyPostRequestBodyDataClass
import com.example.gruppe10_main.dataclasses.LostObjectEnum
import com.example.gruppe10_main.dataclasses.NetcdfEndpoint
import com.example.gruppe10_main.model.DriftyModel
import com.example.gruppe10_main.misc.*
import com.google.gson.Gson
import kotlinx.coroutines.*

/**
 * Class to handle API calls with Drifty, file downloads and parsing of downloaded file.
 */
class DriftyRepository {

    val inputLiveData = MutableLiveData<DriftyModel>()
    val parsedLiveData: MutableLiveData<ArrayList<NetcdfEndpoint?>?> = MutableLiveData()

    private val driftyGetRequester = DriftyGetRequester()
    private val driftyPostRequester = DriftyPostRequester()
    private val fileDownloader = FileDownloader(this)
    private val parser = NetcdfParser()
    private val gson = Gson()
    private var coroutine: Job? = null
    private var inProgress = false

    /**
     * Entry method for doing both POST and GET requests to Drifty. Takes a [body] made from
     * runtime user input. Cancels working coroutine if invoked when not already finished.
     * Invokes NetCDF parser with .nc file download URL.
     */
    fun call(body: DriftyPostRequestBodyDataClass) {
        Log.d(TAG, "Preparing a POST request to Drifty")

        inProgress = true
        if (coroutine?.isActive == true) cancelCoroutine()

        coroutine = GlobalScope.launch(Dispatchers.IO) {

            // Get URL
            val fileUrl: String?
            val extension = driftyPostRequester.post(gson.toJson(body))
            if (extension == null) cancel()

            // Get data from URL
            fileUrl = driftyGetRequester.get(gson, extension)

            // Download data from URL
            if (fileUrl != null) fileDownloader.download(fileUrl)
            else {
                Log.d(TAG, "File download URL not retrieved")
                notifyRequestFailed()
            }

            inProgress = false

            // For testing without API delay.
            //fileDownloader.download("https://in2000.drifty.met.no/results/ba0db36a-f30e-409c-b8c1-8323ab162c8d/results.nc")
        }
    }

    /**
     * Remove all points from LiveData model.
     */
    fun resetPoints() {
        val emptyList = ArrayList<NetcdfEndpoint?>()
        emptyList.add(null)
        parsedLiveData.postValue(emptyList)
    }

    /**
     * Notify MutableLiveData of download finished by posting parsed data to [parsedLiveData].
     */
    fun notifyDownloadFinished(pathname: String) = parsedLiveData.postValue(parser.parse(pathname))

    /**
     * Returns the boolean [inProgress].
     */
    fun isInProgress() = inProgress

    /**
     * Set [inProgress] to false.
     */
    fun setProgressSuccessful() { inProgress = false }

    /**
     * Cancel working coroutine and notify view elements that API request failed by posting
     * null to [parsedLiveData].
     */
    fun notifyRequestFailed() {
        cancelCoroutine()
        parsedLiveData.postValue(null)
    }

    /**
     * Cancel working coroutine for handling asynchronous API calls.
     */
    fun cancelCoroutine() {
        Log.d(TAG, "Cancelling coroutine $coroutine")
        inProgress = false
        coroutine?.cancel()
    }
}
package com.example.gruppe10_main.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.gruppe10_main.R
import com.example.gruppe10_main.dataclasses.DriftyPostRequestBodyDataClass
import com.example.gruppe10_main.dataclasses.LeewayObjectTypeMap
import com.example.gruppe10_main.dataclasses.drifty.*
import com.example.gruppe10_main.misc.*
import com.example.gruppe10_main.model.*
import com.example.gruppe10_main.repositories.DriftyRepository
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

/**
 * ViewModel for handling business logic related to [DriftyRepository] and [DriftyModel].
 *
 */
class DriftyViewModel: ViewModel() {

    // user input validator object
    private val validator = InputValidator()

    // for handling API requests
    private val repository = DriftyRepository()

    // for changing user input data created during runtime
    val bodyModel = DriftyModel()

    // a LiveData representation of user input
    val liveInputData = repository.inputLiveData

    // a LiveData representation of endpoints delivered by the Drifty API
    val parsedLiveData = repository.parsedLiveData

    // for checking if date is in the past or not
    var validDate = true

    /**
     * Validates inputs and pass data to [repository].
     */
    fun callDrifty() = repository.call(getBodyInJsonFormat())


    /**
     * For facilitating communication between fragment and model.
     */
    fun isInProgress() = repository.isInProgress()
    fun setProgressSuccessful() = repository.setProgressSuccessful()
    fun timeIsInPast(time: String, date: String) = validator.timeIsInPast(time, date)
    fun cancelCall() = repository.cancelCoroutine()
    fun clearCache() = repository.resetPoints()

    /**
     * For handling value updates given by user.
     */
    fun setCoordinates(inputLat: Double?, inputLon: Double?) =
        liveInputData.postValue(bodyModel.setCoordinates(inputLat, inputLon))

    fun setRadius(inputRadius: Int) =
        liveInputData.postValue(bodyModel.setRadius(inputRadius))

    fun setTime(inputTime: String) =
        liveInputData.postValue(bodyModel.setTime(inputTime))

    fun setDate(inputDate: String) =
        liveInputData.postValue(bodyModel.setDate(inputDate))

    fun setObjectType(inputObjectType: String) =
        liveInputData.postValue(bodyModel.setObjectType(inputObjectType))

    /**
     * Returns a data class of the variables contained in this object, ready to be serialized.
     */
    private fun getBodyInJsonFormat(): DriftyPostRequestBodyDataClass {
        val model = "leeway"
        val seed = Seed(LeewayObjectTypeMap.map[bodyModel.objectType].toString())
        val configuration = Configuration(seed)
        val timestamp = String.format("%s:00Z",
                (LocalDateTime.parse(String.format("%sT%s:00", bodyModel.date, bodyModel.time))
                        - Duration.ofHours(1)).toString())
        val now = String.format("%s:00Z", LocalDateTime.now().toString().substring(0, 16))
        val from = From(bodyModel.latitude, bodyModel.longitude, bodyModel.radius, timestamp)
        val to = To(bodyModel.latitude, bodyModel.longitude, bodyModel.radius, now)
        val cone = Cone(from, to)
        val geo = Geo(cone)

        return DriftyPostRequestBodyDataClass(model, geo, bodyModel.hours, configuration)
    }

    /**
     * Method that validates all user input that are store in [bodyModel].
     * Invoked by SearchButtonFragment.
     */
    fun validateInput(): Boolean {
        try {
            validator.checkLatitude(bodyModel.latitude)
            validator.checkLongitude(bodyModel.longitude)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            ViewUtility.createToast(
                App.getContext().resources.getString(R.string.coordinates_exception_msg),
                Toast.LENGTH_SHORT)
            return false
        }

        try {
            validator.checkRadius(bodyModel.radius)
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, e.toString())
            return false
        }

        try {
            validator.checkDate(bodyModel.date)
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, e.toString())
            return false
        }

        try {
            validator.checkTime(bodyModel.time)
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, e.toString())
            return false
        }

        try {
            validator.checkSimulationDuration(bodyModel.hours)
        } catch (e: Exception) {
            when (e) {
                is IllegalArgumentException, is DateTimeParseException -> {
                    Log.e(TAG, e.toString())
                    ViewUtility.createToast(String.format(
                            App.getContext().resources.getString(R.string.input_time_exception_msg,
                                bodyModel.time, bodyModel.date)),
                            Toast.LENGTH_SHORT)
                    return false
                }
            }
        }

        return true
    }
}

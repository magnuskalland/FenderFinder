@file:Suppress("NAME_SHADOWING")
package com.example.gruppe10_main.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.example.gruppe10_main.R
import com.example.gruppe10_main.misc.App
import com.example.gruppe10_main.misc.MAX_DAYS_IN_PAST
import com.example.gruppe10_main.misc.TAG
import com.example.gruppe10_main.misc.Utility.toStringWithZero
import com.example.gruppe10_main.misc.ViewUtility
import com.example.gruppe10_main.viewmodel.DriftyViewModel
import java.util.*

/**
 * Class for user to change input time and date.
 */
class TimeFragment: ParentFragment(R.layout.fragment_time) {

    private val buttonSaveFragment = ButtonSaveFragment()
    private lateinit var driftyViewModel: DriftyViewModel
    private lateinit var timePicker: TimePicker
    private lateinit var calendarView: CalendarView

    private lateinit var timeString: String
    private lateinit var dateString: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.v(TAG, "TimeFragment onViewCreated")
        changeFragment(R.id.bottom_container, buttonSaveFragment, false)

        timePicker = view.findViewById(R.id.timePicker)
        calendarView = view.findViewById(R.id.calendarView)
        driftyViewModel = ViewModelProvider(requireActivity()).get(DriftyViewModel::class.java)

        configureCalendarView()
        configureTimePicker()
    }

    /**
     * Configures the [CalendarView] of this class.
     */
    private fun configureCalendarView() {
        val today: Calendar = Calendar.getInstance()
        val maxDaysInPast: Calendar = today.clone() as Calendar
        maxDaysInPast.add(Calendar.DATE, - MAX_DAYS_IN_PAST)
        calendarView.minDate = maxDaysInPast.timeInMillis
        calendarView.maxDate = today.timeInMillis

        dateString = "${driftyViewModel.bodyModel.date.substring(0, 4)}-" +
                "${driftyViewModel.bodyModel.date.substring(5, 7)}-" +
                driftyViewModel.bodyModel.date.substring(8, 10)

        val dateInMillis = convertToMillis(driftyViewModel.bodyModel.date.substring(0, 4),
            driftyViewModel.bodyModel.date.substring(5, 7),
            driftyViewModel.bodyModel.date.substring(8, 10))

        calendarView.setDate(dateInMillis, true, true)

        calendarView.setOnDateChangeListener { _, year, monthOfYear, dayOfMonth ->
            dateString = "$year-${(monthOfYear + 1).toStringWithZero()}-${dayOfMonth.toStringWithZero()}"
            if (driftyViewModel.timeIsInPast(timeString, dateString)) {
                driftyViewModel.setDate(dateString)
                driftyViewModel.validDate = true
            }
            else {
                ViewUtility.createToast(App.getContext().resources.getString(R.string.wrong_date), Toast.LENGTH_SHORT)
                driftyViewModel.validDate = false
            }
        }
    }

    /**
     * Configures the [TimePicker] of this class.
     */
    private fun configureTimePicker() {
        timePicker.setIs24HourView(true)
        // Setting time based on view model
        timePicker.hour = driftyViewModel.bodyModel.time.substring(0, 2).toInt()
        timePicker.minute = driftyViewModel.bodyModel.time.substring(3, 5).toInt()

        timeString = "${timePicker.hour.toStringWithZero()}:${timePicker.minute.toStringWithZero()}"

        timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            timeString = "${hourOfDay.toStringWithZero()}:${minute.toStringWithZero()}"
            Log.d(TAG, timeString)
            if (driftyViewModel.timeIsInPast(timeString, dateString)){
                driftyViewModel.setTime(timeString)
                driftyViewModel.validDate = true
            }
            else {
                ViewUtility.createToast(App.getContext().resources.getString(R.string.wrong_date), Toast.LENGTH_SHORT)
                driftyViewModel.validDate = false
            }
        }
    }

    /**
     * Converts a given date of [year], [month] and [day]. Is used to set current date as default
     * date in [calendarView]
     */
    private fun convertToMillis(year: String, month: String, day: String): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year.toInt())
            set(Calendar.MONTH, month.toInt() - 1)
            set(Calendar.DAY_OF_MONTH, day.toInt())
        }

        return calendar.timeInMillis
    }
}
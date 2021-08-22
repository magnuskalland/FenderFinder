package com.example.gruppe10_main.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.gruppe10_main.R
import com.example.gruppe10_main.model.DriftyModel
import com.example.gruppe10_main.misc.TAG
import com.example.gruppe10_main.misc.Utility
import com.example.gruppe10_main.viewmodel.DriftyViewModel

/**
 * Class for displaying an overview of the user's input.
 * Intersects the user's interaction with [GpsFragment], [ItemFragment], [RadiusFragment] and
 * [TimeFragment], and enables user to initiate a search by changing bottom fragment to
 * [ButtonSearchFragment].
 */
class OverviewFragment: ParentFragment(R.layout.fragment_overview) {

    private lateinit var driftyViewModel: DriftyViewModel

    private val buttonSearchFragment = ButtonSearchFragment()
    private val gpsFragment = GpsFragment()
    private val timeFragment = TimeFragment()
    private val radiusFragment = RadiusFragment()
    private val itemFragment = ItemFragment()

    private lateinit var coordinatesButton: Button
    private lateinit var timeButton: Button
    private lateinit var radiusButton: Button
    private lateinit var itemButton: Button

    private lateinit var coordinatesText: TextView
    private lateinit var timeText: TextView
    private lateinit var radiusText: TextView
    private lateinit var itemText: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.v(TAG, "OverviewFragment onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        coordinatesButton = view.findViewById(R.id.gpsCoordinatesButton)
        timeButton = view.findViewById(R.id.timeButton)
        radiusButton = view.findViewById(R.id.radiusButton)
        itemButton = view.findViewById(R.id.itemButton)

        coordinatesText = view.findViewById(R.id.gpsData)
        timeText = view.findViewById(R.id.timeData)
        radiusText = view.findViewById(R.id.radiusData)
        itemText = view.findViewById(R.id.itemData)

        changeFragment(R.id.bottom_container, buttonSearchFragment, false)
        populateData(DriftyModel())
        initializeOnClickListeners()

        driftyViewModel = ViewModelProvider(requireActivity()).get(DriftyViewModel::class.java)
        driftyViewModel.liveInputData.observe(viewLifecycleOwner) { populateData(it) }
    }

    /**
     * Sets data based on values contained in [model].
     */
    private fun populateData(model: DriftyModel) {
        if (model.latitude == null || model.longitude == null) {
            coordinatesText.text = resources.getString(R.string.missing_coordinates)

        } else {
            coordinatesText.text = String.format("%s\n%s",
                Utility.decimalDegreesToDegreeMinute(model.latitude!!, true),
                Utility.decimalDegreesToDegreeMinute(model.longitude!!, false))
        }

        timeText.text = String.format(
                "%s - %s",
                dateToString(model.date),
                model.time.substring(0, 5))

        radiusText.text = String.format("%s m", model.radius)
        itemText.text = model.objectType
    }

    /**
     * Sets all click listeners for buttons contained in this class.
     */
    private fun initializeOnClickListeners() {
        coordinatesButton.setOnClickListener {
            if (driftyViewModel.bodyModel.latitude == null ||
                driftyViewModel.bodyModel.longitude == null) {
                    requireActivity().supportFragmentManager.popBackStack()
            } else {
                changeFragment(R.id.content_container, gpsFragment, true)
            }
        }

        timeButton.setOnClickListener {
            changeFragment(R.id.content_container, timeFragment, true)
        }

        radiusButton.setOnClickListener {
            changeFragment(R.id.content_container, radiusFragment, true)
        }

        itemButton.setOnClickListener {
            changeFragment(R.id.content_container, itemFragment, true)
        }
    }

    /**
     * Returns a formatted view of [date] by mapping it to the month list from the string array
     * resources.
     */
    private fun dateToString(date: String): String {
        return "${date.substring(8, 10)}. " +
            resources.getStringArray(R.array.months)[date.substring(5, 7).toInt() - 1]
    }
}
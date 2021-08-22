package com.example.gruppe10_main.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.gruppe10_main.R
import com.example.gruppe10_main.misc.TAG
import com.example.gruppe10_main.misc.Utility
import com.example.gruppe10_main.viewmodel.DriftyViewModel

/**
 * Fragment class for displaying current coordinates for user's lost item. Displayed text
 * is provided by the shared [DriftyViewModel].
 * Invoked by [OverviewFragment].
 */
class GpsFragment: ParentFragment(R.layout.fragment_gps) {

    private val buttonSaveFragment = ButtonSaveFragment()
    private lateinit var driftyViewModel: DriftyViewModel
    private lateinit var backToMapButton: Button

    private lateinit var latitudeText: TextView
    private lateinit var longitudeText: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.v(TAG, "GpsFragment onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        changeFragment(R.id.bottom_container, buttonSaveFragment, false)

        latitudeText = view.findViewById(R.id.latitude)
        longitudeText = view.findViewById(R.id.longitude)
        driftyViewModel = ViewModelProvider(requireActivity()).get(DriftyViewModel::class.java)

        backToMapButton = view.findViewById(R.id.backToMapButton)
        backToMapButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
            requireActivity().supportFragmentManager.popBackStack()
        }

        setText()

    }

    private fun setText() {
        if (driftyViewModel.bodyModel.latitude == null
                || driftyViewModel.bodyModel.longitude == null) {
            latitudeText.text = ""
            longitudeText.text = ""
            return
        }

        latitudeText.text =
                Utility.decimalDegreesToDegreeMinute(driftyViewModel.bodyModel.latitude!!, true)
        longitudeText.text =
                Utility.decimalDegreesToDegreeMinute(driftyViewModel.bodyModel.longitude!!, false)
    }
}
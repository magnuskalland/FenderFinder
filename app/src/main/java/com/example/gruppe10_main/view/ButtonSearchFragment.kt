package com.example.gruppe10_main.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.gruppe10_main.R
import com.example.gruppe10_main.misc.*
import com.example.gruppe10_main.misc.ViewUtility.setVisible
import com.example.gruppe10_main.viewmodel.DriftyViewModel

/**
 * Class for initializing call to Drifty. Invoked by [OverviewFragment].
 */
class ButtonSearchFragment: ParentFragment(R.layout.fragment_button_search) {

    private lateinit var driftyViewModel: DriftyViewModel
    private lateinit var searchButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.v(TAG, "ButtonSearchFragment onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        driftyViewModel = ViewModelProvider(requireActivity()).get(DriftyViewModel::class.java)

        searchButton = view.findViewById(R.id.search_button)
        searchButton.setOnClickListener {
            if (driftyViewModel.validateInput()) {
                driftyViewModel.callDrifty()
                parentFragmentManager.popBackStack()
            }
        }
    }
}
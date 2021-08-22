package com.example.gruppe10_main.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gruppe10_main.R
import com.example.gruppe10_main.misc.App
import com.example.gruppe10_main.misc.TAG
import com.example.gruppe10_main.misc.ViewUtility
import com.example.gruppe10_main.viewmodel.DriftyViewModel

/**
 * Fragment for input pages. Invoked by [GpsFragment], [ItemFragment], [RadiusFragment] and
 * [TimeFragment].
 * Sets [saveButton] to remove the top [Fragment] from the fragment managers backstack.
 */
class ButtonSaveFragment: Fragment(R.layout.fragment_button_save) {
    private lateinit var driftyViewModel: DriftyViewModel
    private lateinit var saveButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.v(TAG, "ButtonSaveFragment onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        saveButton = view.findViewById(R.id.save_button)
        driftyViewModel = ViewModelProvider(requireActivity()).get(DriftyViewModel::class.java)

        saveButton.setOnClickListener {
            if(driftyViewModel.validDate) parentFragmentManager.popBackStack()
            else ViewUtility.createToast(App.getContext().resources.getString(R.string.wrong_date),
                Toast.LENGTH_SHORT)
        }
    }
}
package com.example.gruppe10_main.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.gruppe10_main.R
import com.example.gruppe10_main.misc.MAX_RADIUS
import com.example.gruppe10_main.misc.TAG
import com.example.gruppe10_main.viewmodel.DriftyViewModel

/** This class sets the raidus of uncertainty that is sent
 *  thru the API-call towards Drifty */

class RadiusFragment: ParentFragment(R.layout.fragment_radius) {

    private val buttonSaveFragment = ButtonSaveFragment()
    private lateinit var driftyViewModel: DriftyViewModel
    private lateinit var textView: TextView
    private lateinit var seekBar: SeekBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v(TAG, "RadiusFragment onViewCreated")
        changeFragment(R.id.bottom_container, buttonSaveFragment, false)
        driftyViewModel = ViewModelProvider(requireActivity()).get(DriftyViewModel::class.java)
        textView = view.findViewById(R.id.textView)
        seekBar = view.findViewById(R.id.seekBar)

        /**Setting up xml-properties for the seekbar*/
        seekBar.max = MAX_RADIUS
        seekBar.progress = driftyViewModel.bodyModel.radius
        textView.text = String.format("%s%s", seekBar.progress, getString(R.string.meter))

        /** Setting up the change listener for the seekbar. Changes in the seekbar is sent to the
         *  the method setRadius(inputRadius: Int) in the dirftyViewModel*/
        seekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                driftyViewModel.setRadius(seekBar.progress)
                textView.text = String.format("%s%s", seekBar.progress, getString(R.string.meter))
            }

            override fun onStartTrackingTouch(seek: SeekBar) { }
            override fun onStopTrackingTouch(seek: SeekBar) { }
        })
    }
}
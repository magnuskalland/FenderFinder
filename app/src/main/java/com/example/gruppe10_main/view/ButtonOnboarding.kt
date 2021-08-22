package com.example.gruppe10_main.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.gruppe10_main.R
import com.example.gruppe10_main.misc.TAG


/**
This class handles the onboarding-fragment and its functionalities. Enables functionality
for the buttons and handles the content.
*/
class ButtonOnboarding: ParentFragment(R.layout.fragment_button_onboarding) {

    private lateinit var back: Button
    private lateinit var next: Button

    private lateinit var imageOnboardings: Array<Int>
    private lateinit var titles: Array<String>
    private lateinit var descriptions: Array<String>

    private var counter = 0

    private lateinit var imageOnboardingView: ImageView
    private lateinit var titleView: TextView
    private lateinit var descriptionView: TextView
    private lateinit var indicatorsContainerView: LinearLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        back = view.findViewById(R.id.back)
        next = view.findViewById(R.id.next)

        imageOnboardingView = requireActivity().findViewById(R.id.imageOnboarding)
        titleView = requireActivity().findViewById(R.id.textTitle)
        descriptionView = requireActivity().findViewById(R.id.textDescription)
        indicatorsContainerView = requireActivity().findViewById(R.id.indicatorsContainer)

        imageOnboardings = arrayOf( R.drawable.ic_logo_fenderfinder,
                                    R.drawable.ic_image_ob2,
                                    R.drawable.ic_image_ob3,
                                    R.drawable.ic_image_ob4,
                                    R.drawable.ic_image_ob5
                                    )

        titles = resources.getStringArray(R.array.titlesOnboarding)
        descriptions = resources.getStringArray(R.array.descriptionOnboarding)

        back.setOnClickListener { backPage() }
        next.setOnClickListener { nextPage() }

        setupIndicator()
        firstPage()
    }

    private fun backPage() {
        if (counter > 0) {
            counter--
            updatePage()
            setCurrentIndicator(counter)
        } else if (counter == 0) parentFragmentManager.popBackStack()
    }

    private fun nextPage() {
        if (counter < titles.size - 1) {
            counter++
            updatePage()
            setCurrentIndicator(counter)
        } else parentFragmentManager.popBackStack()
    }

    private fun updatePage() {
        imageOnboardingView.setImageResource(imageOnboardings[counter])
        titleView.text = titles[counter]
        descriptionView.text = descriptions[counter]
    }

    private fun firstPage() {
        imageOnboardingView.setImageResource(imageOnboardings[counter])
        titleView.text = titles[counter]
        descriptionView.text = descriptions[counter]
        setCurrentIndicator(counter)
    }

    private fun setupIndicator() {
        val indicators = arrayOfNulls<ImageView>(titles.size)
        val layoutParams: LinearLayout.LayoutParams =
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(8,0,0,0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(requireActivity().application)
            indicators[i]?.let {
                it.setImageDrawable(
                        ContextCompat.getDrawable(
                                requireActivity().application,
                                R.drawable.indicator_inactive_background
                        )
                )
                it.layoutParams = layoutParams
                indicatorsContainerView.addView(it)
            }
        }
    }

    private fun setCurrentIndicator(position: Int) {
        for (i in 0 until indicatorsContainerView.childCount) {
            val imageView = indicatorsContainerView.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(
                                requireActivity().application,
                                R.drawable.indicator_active_background
                        )
                )
            } else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(
                                requireActivity().application,
                                R.drawable.indicator_inactive_background
                        )
                )
            }
        }
    }
}
package com.example.gruppe10_main.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.gruppe10_main.R
import com.example.gruppe10_main.misc.TAG
import com.google.android.material.button.MaterialButton

/**
 * Class for containing text and images manipulated in [ButtonOnboarding].
 */
class OnboardingFragment:
    ParentFragment(R.layout.fragment_onboarding) {

    private val buttonOnboarding = ButtonOnboarding()
    private lateinit var skip: ImageButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        skip = view.findViewById(R.id.skip)
        changeFragment(R.id.bottom_container, buttonOnboarding, false)

        skip.setOnClickListener {
            if (parentFragmentManager.backStackEntryCount == 0)
                changeFragment(R.id.content_container, MapboxFragment(), false)
            else parentFragmentManager.popBackStack()
        }
    }
}
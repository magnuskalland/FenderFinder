package com.example.gruppe10_main.view

import androidx.fragment.app.Fragment

/**
 * Super class of all fragments that implements a wrapper for changing fragment.
 */
abstract class ParentFragment(
    contentLayoutId: Int
) : Fragment(contentLayoutId) {

    /**
     * Wrapper method that provides a simple interface for subclasses to change fragment.
     * [currentContainerViewId] is what ViewGroup in [MainActivity] to be changed, [fragment]
     * is the target fragment to inflate the container. Adds transaction to the fragment
     * managers backstack if [addToBackStack] is true.
     */
    fun changeFragment(currentContainerViewId: Int, fragment: Fragment, addToBackStack: Boolean) {
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(currentContainerViewId, fragment)
        if (addToBackStack) fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}
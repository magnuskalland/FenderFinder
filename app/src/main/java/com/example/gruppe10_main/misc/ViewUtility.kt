package com.example.gruppe10_main.misc

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.widget.Toast

/**
 * Static methods for [View] elements.
 */
object ViewUtility {


    // Reusable toast.
    private val toast = Toast.makeText(App.getContext(), "", Toast.LENGTH_SHORT)

    /**
     * Public function for creating and showing a Toast. Takes a [text] to display and a given
     * [length].
     */
    fun createToast(text: String, length: Int) {
        toast.apply {

            // Only works for API level 29 and below
            if (Utility.getApiLevel() < 30) setGravity(Gravity.TOP, 0, 92)

            duration = length
            setText(text)
            show()
        }
    }

    /**
     * Sets this [View] visible.
     */
    fun View.setVisible() {
        this.visibility = View.VISIBLE
    }

    /**
     * Sets this [View] invisible.
     */
    fun View.setInvisible() {
        this.visibility = View.INVISIBLE
    }

    /**
     * Sets this [View] gone.
     */
    fun View.setGone() {
        this.visibility = View.GONE
    }
}
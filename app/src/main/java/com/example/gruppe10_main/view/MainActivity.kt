package com.example.gruppe10_main.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import com.example.gruppe10_main.R
import com.example.gruppe10_main.misc.TAG
import com.example.gruppe10_main.misc.Utility
import com.mapbox.mapboxsdk.Mapbox

/**
 * Main and only Activity of application. Implements lifecycle methods to handle
 * Android's OpenGL lifecycle.
 * Gets an instance of Mapbox with given access token.
 * Invokes [OnboardingFragment] if app is opened for the first time, else
 *
 */
class MainActivity : AppCompatActivity() {
    private val mapFragment = MapboxFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "Running app on API level ${Utility.getApiLevel()}")
        Log.d(TAG, "LIFECYCLE: onCreate")
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
        window.statusBarColor = ContextCompat.getColor(this, R.color.app_blue)
        val firstRun = getSharedPreferences("preference", MODE_PRIVATE)
                .getBoolean("isFirstRun", true)

        supportFragmentManager.commit {
            replace(R.id.content_container, mapFragment)
            addToBackStack(null)
        }

        if (firstRun) {
            supportFragmentManager.commit {
                replace(R.id.content_container, OnboardingFragment())
                addToBackStack(null)
                getSharedPreferences("preference", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isFirstRun", false)
                    .apply()
            }
        }
    }

    /** Activity lifecycle handling **/
    override fun onStart() {
        Log.d(TAG, "Lifecycle: onStart")
        super.onStart()
        mapFragment.mapView?.onStart()
    }

    override fun onResume() {
        Log.d(TAG, "Lifecycle: onResume")
        super.onResume()
        mapFragment.mapView?.onResume()
    }

    override fun onPause() {
        Log.d(TAG, "Lifecycle: onPause")
        super.onPause()
        mapFragment.mapView?.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "Lifecycle: onStop")
        super.onStop()
        mapFragment.mapView?.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG, "Lifecycle: onSaveInstanceState")
        super.onSaveInstanceState(outState)
        mapFragment.mapView?.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        Log.d(TAG, "Lifecycle: onDestroy")
        super.onDestroy()
    }

    override fun onLowMemory() {
        Log.d(TAG, "Lifecycle: onLowMemory")
        super.onLowMemory()
        mapFragment.mapView?.onLowMemory()
    }
}
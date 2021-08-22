package com.example.gruppe10_main

import android.util.Log
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.gruppe10_main.misc.TAG
import com.example.gruppe10_main.view.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest{

    //Setup of rule to get scenario of activity. Will be invoked locally in each function
    @get: Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    //Test bottom container
    @Test
    fun test_initialBottomFragment() {
        //check that weatherFragment is displayed
        onView(withId(R.id.weatherFragment)).check(matches(isDisplayed()))
        Log.d(TAG, "UI-test, weatherFragment tested")
    }

    //Test main content container
    @Test
    fun test_initialMainFragment() {
        //check that mapfragment is displayed
        onView(withId(R.id.mapFragment)).check(matches(isDisplayed()))
        Log.d(TAG, "UI-test, mapFragment tested")

        //check that zoomIn-button is displayed and clickable
        onView(withId(R.id.zoomIn)).check(matches(isDisplayed()))
        onView(withId(R.id.zoomIn)).perform(click())
        Log.d(TAG, "UI-test, zoomIn tested")

        //check that zoomOut-button is displayed and clickable
        onView(withId(R.id.zoomOut)).check(matches(isDisplayed()))
        onView(withId(R.id.zoomOut)).perform(click())
        Log.d(TAG, "UI-test, zoomOut tested")

        //check that myLocation-button is displayed and clickable
        onView(withId(R.id.myLocation)).check(matches(isDisplayed()))
        onView(withId(R.id.myLocation)).perform(click())
        Log.d(TAG, "UI-test, myLocation tested")

        //check that overviewFragment doesn't exist
        onView(withId(R.id.overviewFragment)).check(doesNotExist())
        Log.d(TAG, "UI-test, overviewFragment tested")

        //check that next_fragment_button is displayed and clickable
        onView(withId(R.id.next_fragment_button)).check(matches(isDisplayed()))
        onView(withId(R.id.next_fragment_button)).perform(click())
        Log.d(TAG, "UI-test, next_fragment_button tested")

        //check that overview fragment is displayed
        onView(withId(R.id.overviewFragment)).check(matches(isDisplayed()))
        Log.d(TAG, "UI-test, overviewFragment tested")
    }
}
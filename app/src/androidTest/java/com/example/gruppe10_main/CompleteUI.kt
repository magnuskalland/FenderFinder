package com.example.gruppe10_main


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.GrantPermissionRule
import com.example.gruppe10_main.view.MainActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class CompleteUI {

    //Setup of rule to get scenario of activity. Will be invoked locally in each function
    @get: Rule
    var mActivityTestRule = ActivityScenarioRule(MainActivity::class.java)

    @get: Rule
    var mGrantPermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(
            "android.permission.ACCESS_FINE_LOCATION"
        )

    @Test
    fun uiTest() {
        //onboarding-button
        val appCompatImageButton = onView(
            allOf(
                withId(R.id.onboardingAgain),
                childAtPosition(
                    allOf(
                        withId(R.id.mapFragment),
                        childAtPosition(
                            allOf(
                                withId(R.id.content_container),
                                withContentDescription("mapboxView")
                            ),
                            0
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        //skip-button
        val appCompatImageButton11 = onView(
            allOf(withId(R.id.skip),
                childAtPosition(
                    childAtPosition(
                        allOf(withId(R.id.content_container),
                            withContentDescription("mapboxView")),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatImageButton11.perform(click())

        //myLocation-button
        val appCompatImageButton2 = onView(
            allOf(
                withId(R.id.myLocation), withContentDescription("Min posisjon"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.mapFragment),
                        5
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatImageButton2.perform(click())

        //zoomIn-button
        val appCompatImageButton3 = onView(
            allOf(
                withId(R.id.zoomIn), withContentDescription("Zoom inn"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.mapFragment),
                        5
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton3.perform(click())

        //zoomOut-button
        val appCompatImageButton4 = onView(
            allOf(
                withId(R.id.zoomOut), withContentDescription("Zoom ut"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.mapFragment),
                        5
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageButton4.perform(click())

        //compass
        val compassView = onView(
            allOf(
                withContentDescription("Map compass. Activate to reset the map rotation to North."),
                childAtPosition(
                    allOf(
                        withId(R.id.mapView),
                        withContentDescription("Showing a Map created with Mapbox. Scroll by dragging two fingers. Zoom by pinching two fingers."),
                        childAtPosition(
                            withId(R.id.mapFragment),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        compassView.perform(click())

        //next_fragment_button
        val appCompatButton = onView(
            allOf(withId(R.id.next_fragment_button), withText("Velg stedet der du \n mistet gjenstanden din"),
                childAtPosition(
                    allOf(withId(R.id.mapFragment),
                        childAtPosition(
                            allOf(withId(R.id.content_container), withContentDescription("mapboxView")),
                            0)),
                    1),
                isDisplayed()))
        appCompatButton.perform(click())

        //gpsCoordinatesButton
        val appCompatButton2 = onView(
            allOf(
                withId(R.id.gpsCoordinatesButton),
                childAtPosition(
                    allOf(
                        withId(R.id.box_gps),
                        childAtPosition(
                            withId(R.id.overviewFragment),
                            2
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        //perform click and return to map
        appCompatButton2.perform(click())

        //next_fragment_button
        val appCompatButton3 = onView(
            allOf(withId(R.id.next_fragment_button), withText("Velg stedet der du \n mistet gjenstanden din"),
                childAtPosition(
                    allOf(withId(R.id.mapFragment),
                        childAtPosition(
                            allOf(withId(R.id.content_container), withContentDescription("mapboxView")),
                            0)),
                    1),
                isDisplayed()))
        appCompatButton3.perform(click())

        //timeButton
        val appCompatButton4 = onView(
            allOf(
                withId(R.id.timeButton),
                childAtPosition(
                    allOf(
                        withId(R.id.box_time),
                        childAtPosition(
                            withId(R.id.overviewFragment),
                            3
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatButton4.perform(click())

        //save_button
        val appCompatButton5 = onView(
            allOf(
                withId(R.id.save_button), withText("Lagre"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottom_container),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatButton5.perform(click())

        //radiusButton
        val appCompatButton6 = onView(
            allOf(
                withId(R.id.radiusButton),
                childAtPosition(
                    allOf(
                        withId(R.id.box_radius),
                        childAtPosition(
                            withId(R.id.overviewFragment),
                            4
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatButton6.perform(click())

        //save_button
        val appCompatButton7 = onView(
            allOf(
                withId(R.id.save_button), withText("Lagre"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottom_container),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatButton7.perform(click())

        //itemButton
        val appCompatButton8 = onView(
            allOf(
                withId(R.id.itemButton),
                childAtPosition(
                    allOf(
                        withId(R.id.box_item),
                        childAtPosition(
                            withId(R.id.overviewFragment),
                            5
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatButton8.perform(click())

        //list of items
        val appCompatCheckedTextView = onData(anything())
            .inAdapterView(
                allOf(
                    withId(R.id.listView),
                    childAtPosition(
                        withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                        3
                    )
                )
            )
            .atPosition(3)
        //perform click, selects "Bensintank med innhold"
        appCompatCheckedTextView.perform(click())

        //save_button
        val appCompatButton9 = onView(
            allOf(
                withId(R.id.save_button), withText("Lagre"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottom_container),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatButton9.perform(click())

        //search_button
        val appCompatButton10 = onView(
            allOf(
                withId(R.id.search_button), withText("Søk"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottom_container),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatButton10.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}

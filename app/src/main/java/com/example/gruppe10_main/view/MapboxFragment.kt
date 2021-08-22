package com.example.gruppe10_main.view

// Project
import com.example.gruppe10_main.R
import com.example.gruppe10_main.dataclasses.NetcdfEndpoint
import com.example.gruppe10_main.misc.*
import com.example.gruppe10_main.misc.ViewUtility.setGone
import com.example.gruppe10_main.misc.ViewUtility.setInvisible
import com.example.gruppe10_main.misc.ViewUtility.setVisible
import com.example.gruppe10_main.viewmodel.DriftyViewModel
import com.example.gruppe10_main.viewmodel.MapboxViewModel
import com.example.gruppe10_main.dataclasses.LostObjectEnum

// Android native
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Color
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import java.lang.ref.WeakReference

// Mapbox
import com.mapbox.android.core.location.*
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.LocationUpdate
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.module.http.HttpRequestUtil
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.mapboxsdk.utils.BitmapUtils

/**
 * Class that handles map, user interaction with map, tracking location of user and displaying
 * points of estimated search area of lost object.
 *
 * Invokes the [requestPermissionLauncher] first time app is opened.
 *
 * Listens for data updates in [MapboxViewModel] and [DriftyViewModel].
 *
 * Has 3 different interaction modes:
 *
 *      1. default
 *      2. when calling Drifty API
 *      3. after laying out estimated search area.
 */
class MapboxFragment:
    ParentFragment(R.layout.fragment_map),
    OnMapReadyCallback {

    // ViewModels
    private lateinit var driftyViewModel: DriftyViewModel
    private val mapboxViewModel by viewModels<MapboxViewModel>()

    // Fragments
    private val weatherFragment = WeatherFragment()
    private val overviewFragment = OverviewFragment()
    private val onboardingFragment = OnboardingFragment()

    // Views
    var mapView: MapView? = null
    private lateinit var progressView: RelativeLayout
    private lateinit var removePointsView: RelativeLayout

    private lateinit var cancelSearchButton: Button
    private lateinit var nextFragmentButton: Button
    private lateinit var onboardingButton: ImageButton
    private lateinit var myLocationCameraButton: ImageButton
    private lateinit var zoomInButton: ImageButton
    private lateinit var zoomOutButton: ImageButton
    private lateinit var removePointsResponseYes: Button
    private lateinit var removePointsResponseNo: Button
    private lateinit var progressText: TextView
    private lateinit var progressBar: ProgressBar

    // Mapbox objects
    private lateinit var mapboxInterface: MapboxMap
    private lateinit var symbolManagerUserMarker: SymbolManager
    private lateinit var symbolManagerLostObjects: SymbolManager
    private var locationEngine: LocationEngine? = null

    // Current user position
    private var currentLat = 0.0
    private var currentLon = 0.0

    // Input from user during runtime
    private var lostObjectLat: Double? = null
    private var lostObjectLon: Double? = null

    private var interactiveMode = true

    private lateinit var callback: MainActivityLocationCallback

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            isGranted: Boolean ->
                if (isGranted) {
                    Log.d(TAG, "Location access granted")
                    mapboxInterface.style?.let { enableLocation(it) }

                } else {
                    Log.d(TAG, "Location access denied")
                    val text = resources.getString(R.string.user_location_permission_explanation)
                    ViewUtility.createToast(text, Toast.LENGTH_LONG)
                    moveCameraToLocation(DEFAULT_LATITUDE, DEFAULT_LONGITUDE, ZOOM_LEVEL_LOW)
                }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        HttpRequestUtil.setLogEnabled(false)
        Log.v(TAG, "MapFragment onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        changeFragment(R.id.bottom_container, weatherFragment, false)

        initViews(view)
        setOnClickListeners()

        driftyViewModel = ViewModelProvider(requireActivity()).get(DriftyViewModel::class.java)

        if (!coordinatesIsNull()) setNextFragmentButtonActive()
        else setNextFragmentButtonInactive()

        callback = MainActivityLocationCallback(activity as MainActivity)
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)

        // If coming back to map after calling Drifty
        if (driftyViewModel.isInProgress()) toggleProgressView(true)
    }

    /**
     * Initialize all views.
     */
    private fun initViews(view: View) {
        onboardingButton = view.findViewById(R.id.onboardingAgain)
        myLocationCameraButton = view.findViewById(R.id.myLocation)
        zoomInButton = view.findViewById(R.id.zoomIn)
        zoomOutButton = view.findViewById(R.id.zoomOut)
        progressView = view.findViewById(R.id.progressView)
        progressText = view.findViewById(R.id.progressMessage)
        progressBar = view.findViewById(R.id.progressBar)
        removePointsView = view.findViewById(R.id.removePointsView)
        nextFragmentButton = view.findViewById(R.id.next_fragment_button)
        removePointsResponseYes = view.findViewById(R.id.yesButton)
        removePointsResponseNo = view.findViewById(R.id.noButton)
        mapView = view.findViewById(R.id.mapView)
        cancelSearchButton = view.findViewById(R.id.quitButton)
    }

    /**
     * Attach all click listeners.
     */
    private fun setOnClickListeners() {

        nextFragmentButton.setOnClickListener {
            changeFragment(R.id.content_container, overviewFragment, true)
            if (!coordinatesIsNull()) driftyViewModel.setCoordinates(lostObjectLat!!, lostObjectLon!!)
        }

        onboardingButton.setOnClickListener {
            Log.d(TAG, "Onboarding user")
            changeFragment(R.id.content_container, onboardingFragment, true)
        }

        myLocationCameraButton.setOnClickListener { moveCameraToLocation(currentLat, currentLon, ZOOM_LEVEL_HIGH) }
        zoomInButton.setOnClickListener { zoom(ZOOM_INCREMENT) }
        zoomOutButton.setOnClickListener { zoom(-ZOOM_INCREMENT) }

        cancelSearchButton.setOnClickListener {
            Log.d(TAG, "User cancelled search")
            driftyViewModel.cancelCall()
            toggleProgressView(false)
        }

        removePointsResponseYes.setOnClickListener {
            Log.d(TAG, "Removing all points on map")
            setNextFragmentButtonInactive()
            driftyViewModel.clearCache()
            driftyViewModel.setCoordinates(null, null)
            nextFragmentButton.setVisible()
            interactiveMode = true
        }

        removePointsResponseNo.setOnClickListener {
            Log.d(TAG, "Don't hide points")
            removePointsView.setInvisible()
            nextFragmentButton.setVisible()
            setNextFragmentButtonToRemovePoints()
        }
    }

    /**
     * Class that handles actions after location updates.
     * Implements the [LocationEngineCallback] interface.
     */
    private inner class MainActivityLocationCallback(activity: MainActivity):
            LocationEngineCallback<LocationEngineResult> {
        private val activityWeakReference: WeakReference<MainActivity> = WeakReference(activity)
        private val activity = activityWeakReference.get()

        // Runs when device changes location
        override fun onSuccess(result: LocationEngineResult?) {
            if (activity != null) {
                val location = result?.lastLocation ?: return

                // If new location
                if (checkIfLocationIsNew(location.latitude, location.longitude)) {
                    currentLat = location.latitude
                    currentLon = location.longitude
                    weatherFragment.viewModel.fetchForecast(currentLat, currentLon)
                }

                val loc = LocationUpdate.Builder()
                        .location(result.lastLocation)
                        .build()

                mapboxInterface.locationComponent.forceLocationUpdate(loc)
            }
        }

        // Runs when device's location can't be found
        override fun onFailure(exception: Exception) {
            exception.localizedMessage?.toString()?.let { Log.d(TAG, it) }
        }
    }

    /**
     * Invoked by method getMapAsync. Inherited from the [OnMapReadyCallback] interface.
     * Sets [mapboxInterface] to [mapboxMap].
     * Initializes map styling and location tracking. Add styling programmatically to
     * 'setStyle' method.
     */
    override fun onMapReady(mapboxMap: MapboxMap) {
        Log.d(TAG, "onMapReady")
        mapboxInterface = mapboxMap

        // Add styling to map
        mapboxInterface.setStyle(mapboxViewModel.getStyle()) {

            /* Add styling programmatically here */
            mapboxInterface.uiSettings.apply {
                setCompassFadeFacingNorth(false)
                setAttributionTintColor(Color.WHITE)
                setCompassMargins(20, 20, 20, 20)
                setLogoMargins(20, 11, 11, 260)
                setAttributionMargins(270, 11, 11, 260)
            }

            enableLocation(it) // Enable location tracking
            initSymbolManagers(it) // Initialize symbol manager
            addViewModelObservers()
            if (lostObjectLat != null && lostObjectLon != null)
                placeIconOnMap(
                        symbolManagerUserMarker,
                        LOST_OBJECT_ICON,
                        LOST_OBJECT_ICON_SIZE,
                        lostObjectLat!!,
                        lostObjectLon!!)
        }

        toggleMapOnClickListener(true)

    }

    /**
     * Sets map clickable depending on if is [mode] is true or false.
     */
    private fun toggleMapOnClickListener(mode: Boolean) {
        if (mode) {
            mapboxInterface.addOnMapClickListener {
                if (clickOnMap(it.latitude, it.longitude)) setNextFragmentButtonActive()
                else if (interactiveMode) setNextFragmentButtonInactive()
                true
            }
        } else {
            // Not working as intended
            mapboxInterface.removeOnMapClickListener { false }
            mapboxInterface.addOnMapClickListener { false }
        }
    }

    /**
     * Sets observers of ViewModels [driftyViewModel] and [mapboxViewModel].
     *  Must be done after initializing symbol managers to handle coming back with points
     *  already loaded in DriftyModel.
     */
    private fun addViewModelObservers() {
        Log.d(TAG, "Adding ViewModel observers")
        driftyViewModel.parsedLiveData.observe(viewLifecycleOwner) {

            // If unsuccessful call
            if (it == null) {
                Log.d(TAG, "Unsuccessful call")
                progressText.text = resources.getString(R.string.progress_failed)
                progressBar.setInvisible()
                setNextFragmentButtonInactive()

            // If points is reset
            } else if (it[0] == null) {
                Log.d(TAG, "Resetting points")
                symbolManagerLostObjects.deleteAll()
                symbolManagerUserMarker.deleteAll()
                setNextFragmentButtonInactive()

            // If new call
            } else if (it.size == 0) {
                Log.d(TAG, "Drifty call in progress")
                toggleProgressView(true)
            }

            // If successful call
            else {
                Log.d(TAG, "Parse data changed, adding ${it.size} points to map...")
                placeLostObjectMarkers(it)
                driftyViewModel.setProgressSuccessful()
                if (!coordinatesIsNull()) moveCameraToLocation(lostObjectLat!!, lostObjectLon!!, ZOOM_LEVEL_HIGH)
                setNextFragmentButtonToRemovePoints()
                interactiveMode = false
            }
        }

        mapboxViewModel.getLiveData().observe(viewLifecycleOwner) {
            lostObjectLat = it.lostObjectMarkerLat
            lostObjectLon = it.lostObjectMarkerLon
        }
    }

    /**
     * If permission is granted, builds and specifies options LocationComponent by calling
     * [initLocationComponent] with arguemnt [loadedMapStyle].
     *
     * If permission is denied, asks user for permission through [requestPermissionLauncher].
     */
    @SuppressLint("MissingPermission")
    fun enableLocation(loadedMapStyle: Style) {
        Log.d(TAG, "enableLocation")

        if (ContextCompat.checkSelfPermission(App.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
            initLocationComponent(loadedMapStyle)
            initLocationEngine()

        } else {
            Log.d(TAG, "Requesting location access permission")
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    /**
     * Sets this class' graphical icon that displays users location. Invoked by [enableLocation]
     */
    @SuppressLint("MissingPermission")
    fun initLocationComponent(loadedMapStyle: Style) {
        Log.d(TAG, "Initializing LocationComponent")

        val customLocationComponentOptions = LocationComponentOptions.builder(App.getContext())
                .pulseEnabled(true)
                .build()

        val locationComponent = mapboxInterface.locationComponent
        val locationComponentActivationOptions = LocationComponentActivationOptions
                .builder(App.getContext(), loadedMapStyle)
                .locationComponentOptions(customLocationComponentOptions)
                .useDefaultLocationEngine(false)
                .build()

        locationComponent.apply {
            activateLocationComponent(locationComponentActivationOptions)
            isLocationComponentEnabled = true
            renderMode = RenderMode.COMPASS
            zoomWhileTracking(ZOOM_LEVEL_HIGH)

            // If app is opened
            if (lostObjectLat == null || lostObjectLon == null) cameraMode = CameraMode.TRACKING
            // If coming back from different fragment
            else moveCameraToLocation(lostObjectLat!!, lostObjectLon!!, ZOOM_LEVEL_HIGH)
        }
    }

    /**
     * Initializes a LocationEngine to handle location updates.
     * Sets interval of checking for updates.
     */
    @SuppressLint("MissingPermission")
    private fun initLocationEngine() {
        Log.d(TAG, "Initializing LocationEngine")
        locationEngine = LocationEngineProvider.getBestLocationEngine(App.getContext())
        val request = LocationEngineRequest.Builder(DEFAULT_INTERVAL_MILLIS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME)
                .build()

        locationEngine?.requestLocationUpdates(request, callback, Looper.getMainLooper())
        locationEngine?.getLastLocation(callback)
    }

    /**
     * This method initializes [symbolManagerUserMarker] and [symbolManagerLostObjects] as an
     * instance of [SymbolManager] class, and adds symbols that are used in this application to
     * that manager.
     */
    private fun initSymbolManagers(style: Style) {
        Log.d(TAG, "Initializing SymbolManager")
        symbolManagerUserMarker = SymbolManager(mapView!!, mapboxInterface, style).apply {
            iconAllowOverlap = true
            iconIgnorePlacement = true
        }

        symbolManagerLostObjects = SymbolManager(mapView!!, mapboxInterface, style).apply {
            iconAllowOverlap = true
            iconIgnorePlacement = true
        }

        // Add vector assets to map style
        val point = ResourcesCompat.getDrawable(
                resources, R.drawable.ic_round_my_location_24, null)
        style.addImage(LOST_OBJECT_ICON, BitmapUtils.getBitmapFromDrawable(point)!!)

        val stranded = ResourcesCompat.getDrawable(
                resources, R.drawable.ic_icon_small_cross, null)
        style.addImage(STRANDED_ICON, BitmapUtils.getBitmapFromDrawable(stranded)!!)

        val active = ResourcesCompat.getDrawable(
                resources, R.drawable.ic_icon_small_circle, null)
        style.addImage(ACTIVE_ICON, BitmapUtils.getBitmapFromDrawable(active)!!)

    }

    /**
     * Handles action for when user clicks on map.
     * [latitude] [longitude] is coordinate of point. Returns true if user pressed on a valid point
     * and [interactiveMode] is true, else false.
     */
    private fun clickOnMap(latitude: Double, longitude: Double): Boolean {

        // Quick fix for disabling onMapClickListener during API call
        if (driftyViewModel.isInProgress()) {
            ViewUtility.createToast(
                    resources.getString(R.string.quit_progress_toast_message),
                    Toast.LENGTH_SHORT)
            return false
        }

        if (!interactiveMode) {
            ViewUtility.createToast(
                    resources.getString(R.string.press_on_non_interactive_map_toast),
                    Toast.LENGTH_SHORT)
            return false
        }

        // Remove existing marker
        symbolManagerUserMarker.deleteAll()

        // If point clicked is on land
        if (!pointIsOnWater(latitude, longitude)) {
            ViewUtility.createToast(resources.getString(R.string.point_on_land_error), Toast.LENGTH_SHORT)
            lostObjectLat = null; lostObjectLon = null
            mapboxViewModel.setLostObjectMarker(lostObjectLat, lostObjectLon)
            driftyViewModel.setCoordinates(lostObjectLat, lostObjectLon)
            return false
        }

        // Saving icon in model
        mapboxViewModel.setLostObjectMarker(latitude, longitude)

        // Placing icon on map
        placeIconOnMap(symbolManagerUserMarker,
                LOST_OBJECT_ICON, LOST_OBJECT_ICON_SIZE,
                latitude, longitude)

        Log.d(TAG, "Clicked on map: ")
        Log.d(TAG, "    latitude:  ${Utility.decimalDegreesToDegreeMinute(latitude, true)}")
        Log.d(TAG, "    longitude: ${Utility.decimalDegreesToDegreeMinute(longitude, false)}")

        return true
    }

    /**
     * Graphical way of deciding whether coordinate [lat], [lon] is on land or water.
     */
    private fun pointIsOnWater(lat: Double, lon: Double): Boolean {
        val pixel = mapboxInterface.projection.toScreenLocation(LatLng(lat, lon))
        return mapboxInterface.queryRenderedFeatures(pixel, WATER).size != 0
    }

    /**
     * Wrapper method [SymbolManager] and [SymbolOptions].
     * Places an icon image of [iconName] of given [lat] and [lon],
     * with the size of [iconSize] with manager [manager].
     */
    private fun placeIconOnMap(manager: SymbolManager, iconName: String, iconSize: Float,
                               lat: Double, lon: Double) {

        val symbolOptions = SymbolOptions()
                .withLatLng(LatLng(lat, lon))
                .withIconImage(iconName)
                .withIconSize(iconSize)

        // Adding symbol to map
        manager.update(manager.create(symbolOptions))
    }

    /**
     * Method that checks whether given coordinate [latitude], [longitude] is of significant
     * change from [currentLat], [currentLon]. Relieves both device and Nowcast API
     * if unnecessary checks.
     */
    private fun checkIfLocationIsNew(latitude: Double, longitude: Double): Boolean {
        return  latitude !in currentLat - (currentLat * MIN_COORDINATE_MOVEMENT_FACTOR)..
                currentLat + (currentLat * MIN_COORDINATE_MOVEMENT_FACTOR) ||
                longitude !in currentLon - (currentLon * MIN_COORDINATE_MOVEMENT_FACTOR)..
                currentLon + (currentLon * MIN_COORDINATE_MOVEMENT_FACTOR)
    }

    /**
     * Method for placing markers on map. [points] is container of [NetcdfEndpoint] to
     * be placed.
     */
    private fun placeLostObjectMarkers(points: ArrayList<NetcdfEndpoint?>?) {
        points?.let {
            for (p in points) {
                p?.let {
                    placeIconOnMap(
                        symbolManagerLostObjects,

                        when (p.type) {
                            LostObjectEnum.ACTIVE -> ACTIVE_ICON
                            LostObjectEnum.STRANDED -> STRANDED_ICON
                            LostObjectEnum.ORIGIN -> LOST_OBJECT_ICON
                        },

                        when (p.type) {
                            LostObjectEnum.ACTIVE -> ACTIVE_ICON_SIZE
                            LostObjectEnum.STRANDED -> STRANDED_ICON_SIZE
                            LostObjectEnum.ORIGIN -> LOST_OBJECT_ICON_SIZE
                        },

                        p.lat, p.lon
                    )
                }

            }

            toggleProgressView(false)
        }
    }

    /**
     * Method to programmatically adjust the graphics [nextFragmentButton] to indicate that it
     * is clickable.
     */
    private fun setNextFragmentButtonActive() {
        Log.d(TAG, "setNextFragmentButtonActive")
        nextFragmentButton.apply {
            setBackgroundColor(ContextCompat.getColor(App.getContext(), R.color.white))
            text = getString(R.string.active_map_fragment_button)
            removePointsView.setGone()
        }
    }

    /**
     * Method to programmatically adjust the graphics [nextFragmentButton] to indicate that it
     * is not clickable.
     */
    private fun setNextFragmentButtonInactive() {
        Log.d(TAG, "setNextFragmentButtonInactive")
        nextFragmentButton.apply {
            setBackgroundColor(ContextCompat.getColor(App.getContext(), R.color.transparent_white))
            text = getString(R.string.inactive_map_fragment_button)
            removePointsView.setGone()
            setOnClickListener {
                changeFragment(R.id.content_container, overviewFragment, true)
                if (!coordinatesIsNull()) driftyViewModel.setCoordinates(lostObjectLat!!, lostObjectLon!!)
            }
        }
    }

    /**
     * Method to programmatically adjust the graphics [nextFragmentButton] to indicate that it
     * remove points from map.
     */
    private fun setNextFragmentButtonToRemovePoints() {
        Log.d(TAG, "setNextFragmentButtonToRemovePoints")
        nextFragmentButton.apply {
            setBackgroundColor(ContextCompat.getColor(App.getContext(), R.color.white))
            text = resources.getString(R.string.hide_points)
            setOnClickListener {
                Log.d(TAG, "removePointsView is set visible")
                removePointsView.setVisible()
                this.setInvisible()
                setOnClickListener {  }
            }
        }
    }

    /**
     * Wrapper method for handling camera zooms. [increment] is the amount of zoom to be added
     * or subtracted from current zoom level.
     */
    private fun zoom(increment: Double) {
        val position = CameraPosition.Builder()
            .zoom(mapboxInterface.cameraPosition.zoom + increment)
            .build()

        mapboxInterface.animateCamera(CameraUpdateFactory.newCameraPosition(position),
                ZOOM_CAMERA_DURATION)
    }

    /**
     * Wrapper method for moving camera to location [lat], [lon] with [zoomLevel].
     */
    private fun moveCameraToLocation(lat: Double, lon: Double, zoomLevel: Double?) {
        Log.d(TAG, "Move camera to ${Utility.decimalDegreesToDegreeMinute(lat, true)} " +
            "${Utility.decimalDegreesToDegreeMinute(lon, false)} with zoom level $zoomLevel")

        val position: CameraPosition = if (zoomLevel != null) {
            CameraPosition.Builder()
                    .target(LatLng(lat, lon))
                    .tilt(MIN_TILT)
                    .zoom(zoomLevel)
                    .build()
        } else {
            CameraPosition.Builder()
                    .target(LatLng(lat, lon))
                    .tilt(MIN_TILT)
                    .build()
        }

        mapboxInterface.animateCamera(
                CameraUpdateFactory.newCameraPosition(position),
                MOVE_CAMERA_DURATION)
    }

    /**
     * Toggling [progressView] on or off given by [mode]. Is invoked by toggled on when application
     * is calling Drifty, and toggled off when it either failed or succeeded.
     */
    private fun toggleProgressView(mode: Boolean) {
        if (mode) {
            Log.d(TAG, "Showing progress view")
            progressView.setVisible()
            progressBar.setVisible()
            progressText.text = resources.getString(R.string.progress_message)
            nextFragmentButton.setInvisible()
        } else {
            Log.d(TAG, "Hiding progress view")
            progressView.setGone()
            nextFragmentButton.setVisible()
        }
    }

    private fun coordinatesIsNull() = lostObjectLat == null || lostObjectLon == null

    /**
     * Lifecycle handling.
     */
    override fun onDestroyView() {
        Log.d(TAG, "Fragment lifecycle: onDestroyView")
        locationEngine?.removeLocationUpdates(callback)
        mapView?.onDestroy()
        super.onDestroyView()
    }
}


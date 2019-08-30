package com.wmapp.ui.home.views

import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.wmapp.R
import com.wmapp.common.AppConstants
import com.wmapp.common.AppUtils
import com.wmapp.networking.NetworkProcessor
import com.wmapp.ui.base.views.BaseActivity
import com.wmapp.ui.splash.views.actions.ILocationReceivedListener
import com.wmapp.ui.splash.views.actions.ILocationTriggers
import com.wmapp.ui.utility.LocationHelper
import kotlinx.android.synthetic.main.activity_home.*

import javax.inject.Inject

class HomeActivity : BaseActivity(), ILocationReceivedListener, OnMapReadyCallback {

    @Inject
    lateinit var mAppUtils: AppUtils
    @Inject
    lateinit var mAppConstants: AppConstants
    @Inject
    lateinit var mNetwork: NetworkProcessor
    private lateinit var mLocationListener: ILocationTriggers
    private lateinit var mHomeActivityImpl: HomeActivityImpl
    private val mTag = HomeActivity::class.java.canonicalName
    lateinit var mMapView: MapView
    lateinit var mGoogleMap: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        getInjectionComponent().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(R.string.available_cars)
        mLocationListener = LocationHelper(this, true)
        mMapView = allPOIMap
        mMapView.onCreate(savedInstanceState)
        mMapView.onResume()

        initiateDataProcess()

    }

    override fun initiateDataProcess() {
        mHomeActivityImpl = HomeActivityImpl(this, mNetwork)
        try {
            MapsInitializer.initialize(application)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mMapView.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()
        mLocationListener.actionStart()
    }

    override fun onPause() {
        super.onPause()
        mLocationListener.actionPause()
    }

    override fun didReceivedLocation(location: Location) {
        logD(mTag, "Location is ${location.latitude} || ${location.longitude}")
        mHomeActivityImpl.showUpdatedLocation(LatLng(location.latitude, location.longitude))
    }

    override fun locaitonUpdationFailure(message: String) {
        showToast(getString(R.string.failed_to_update_location))
        logD(mTag, "!! locaitonUpdationFailure !!")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            AppConstants.REQUEST_CHECK_SETTINGS ->
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        mLocationListener.actionActivityResultOK()
                    }
                    Activity.RESULT_CANCELED -> {
                        mLocationListener.actionActivityResultCancelled()
                    }
                }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == AppConstants.REQUEST_PERMISSIONS_REQUEST_CODE) {
            mLocationListener.actionRequestPermissionResult(grantResults)
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mGoogleMap = googleMap!!
        mGoogleMap.uiSettings.isMapToolbarEnabled = false
        mHomeActivityImpl.onActivityMapReady(googleMap)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}

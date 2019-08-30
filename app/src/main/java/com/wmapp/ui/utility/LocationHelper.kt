package com.wmapp.ui.utility

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.wmapp.R
import com.wmapp.common.AppConstants
import com.wmapp.ui.base.views.BaseActivity
import com.wmapp.ui.home.views.HomeActivity
import com.wmapp.ui.splash.views.actions.ILocationActions
import com.wmapp.ui.splash.views.actions.ILocationTriggers
import com.wmapp.ui.splash.views.MainActivityImpl
import java.text.DateFormat
import java.util.*

class LocationHelper (var context: BaseActivity,var continueLoc: Boolean) : ILocationActions,
    ILocationTriggers
{
    private var mFusedLocationClient : FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private var mSettingsClient : SettingsClient = LocationServices.getSettingsClient(context)
    private lateinit var mLocationCallback : LocationCallback
    private var mCurrentLocation : Location? = null
    private lateinit var mLastUpdateTime : String
    private var mLocationReceivedStatus : Boolean = false
    private var mRequestingLocationUpdates : Boolean = false
    private lateinit var mLocationRequest : LocationRequest
    private val UPDATE_INTERVAL_IN_MILLISECONDS : Long = 10000
    private val mTag = MainActivityImpl::class.java.simpleName
    private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS : Long = UPDATE_INTERVAL_IN_MILLISECONDS / 2
    private lateinit var mLocationSettingsRequest : LocationSettingsRequest
    private var mLocationDeniedNumber : Int

    init {
        createLocationCallback()
        createLocationRequest()
        buildLocationSettingsRequest()
        mLocationDeniedNumber = 0
    }

    override fun actionStart(){
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates()
        }
        else if (!checkPermissions())
        {
            requestPermissions()
        }else{
            if (!mRequestingLocationUpdates) {
                mRequestingLocationUpdates = true
                startLocationUpdates()
            }
        }
    }

    override fun actionPause(){
        if (mRequestingLocationUpdates) {
            stopLocationUpdates()
        }
    }

    override fun actionActivityResultOK(){
        startLocationUpdates()
    }

    override fun actionActivityResultCancelled(){
        mRequestingLocationUpdates = false
        updateUI()
    }

    override fun actionRequestPermissionResult(grantResults: IntArray){
        if (grantResults.size <= 0) {
            context.logD(mTag, "User interaction was cancelled.")
        }
        else if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            if (!mRequestingLocationUpdates) {
                context.logD(mTag,"if (!mRequestingLocationUpdates)")
                mRequestingLocationUpdates = true
                startLocationUpdates()
            }
        }
        else{
            AppConstants.LOCATION_PERMISSION_DENIED = true
            if(context is HomeActivity){
                (context as HomeActivity).locationUpdationFailure(context.getString(R.string.permission_denied_condition))
            }

        }
    }

    @SuppressLint("MissingPermission")
    override fun startLocationUpdates(){
        val task = mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
        task.addOnSuccessListener{
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback, Looper.myLooper())
            updateUI()
        }
        task.addOnFailureListener{
                data ->
            if (data is ResolvableApiException) {
                try {
                    data.startResolutionForResult(context as Activity,AppConstants.REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {

                    if(context is HomeActivity) {
                        (context as HomeActivity).locationUpdationFailure(context.getString(R.string.permission_denied_condition))
                    }
                }
            }else{
                if(context is HomeActivity) {
                    (context as HomeActivity).locationUpdationFailure (context.getString(R.string.permission_denied_condition))
                }
            }
        }
    }

    override fun requestPermissions(){
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(context,
            Manifest.permission.ACCESS_FINE_LOCATION)
        if (shouldProvideRationale) {
            showAlertDialogInfo(R.string.permission_rationale)
        }
        else
        {
            ActivityCompat.requestPermissions(context,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                AppConstants.REQUEST_PERMISSIONS_REQUEST_CODE)
        }
    }

    private fun updateUI(){
        updateLocationData()
    }

    override fun createLocationCallback(){
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)

                mCurrentLocation = locationResult?.lastLocation
                mLastUpdateTime = DateFormat.getTimeInstance().format(Date())
                updateLocationData()
            }
        }
    }

    fun updateLocationData(){
        try {
            if (mCurrentLocation != null) {
                mLocationReceivedStatus = true

                if(context is HomeActivity) {
                    (context as HomeActivity).didReceivedLocation(mCurrentLocation as Location)
                }

                if(!continueLoc)
                    stopLocationUpdates()
            }
        } catch (e: Exception) {
            context.logE(mTag,"updateLocationData ${e.toString()}")
        }
    }

    override fun createLocationRequest(){
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    fun showPermissionDialog(){
        ActivityCompat.requestPermissions(context,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            AppConstants.REQUEST_PERMISSIONS_REQUEST_CODE)
    }

    override fun buildLocationSettingsRequest(){
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)
        mLocationSettingsRequest = builder.build()
    }

    override fun stopLocationUpdates(){
        var task = mFusedLocationClient.removeLocationUpdates(mLocationCallback)
        task.addOnCompleteListener {
            mRequestingLocationUpdates = false
        }
    }

    override fun checkPermissions(): Boolean{
        val permissionState  =
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun showAlertDialogInfo(mainTextStringId: Int){

        context.showAlert(mainTextStringId,R.string.ok,R.string.cancel,
            DialogInterface.OnClickListener { dialog, which ->
                showPermissionDialog()
            },
            DialogInterface.OnClickListener { dialog, which ->
                AppConstants.LOCATION_PERMISSION_DENIED = true
                if(context is HomeActivity) {
                    (context as HomeActivity).locationUpdationFailure(context.getString(R.string.permission_denied_condition))
                }
            })
    }

}
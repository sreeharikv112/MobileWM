package com.wmapp.ui.splash.views.actions

interface ILocationActions {

    fun checkPermissions(): Boolean
    fun stopLocationUpdates()
    fun requestPermissions()

    fun createLocationCallback()
    fun createLocationRequest()
    fun buildLocationSettingsRequest()
    fun startLocationUpdates()
}
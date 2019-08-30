package com.wmapp.ui.splash.views.actions

/**
 * Location related invocation and call backs.
 */
interface ILocationActions {

    fun checkPermissions(): Boolean
    fun stopLocationUpdates()
    fun requestPermissions()

    fun createLocationCallback()
    fun createLocationRequest()
    fun buildLocationSettingsRequest()
    fun startLocationUpdates()
}
package com.wmapp.ui.splash.views.actions

import android.location.Location

interface ILocationReceivedListener {

    fun didReceivedLocation(location: Location)

    fun locaitonUpdationFailure(message: String)
}
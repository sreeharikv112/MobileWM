package com.wmapp.ui.splash.views.actions

import android.location.Location

/**
 * Call back for successful and failure location updation scenario.
 */
interface ILocationReceivedListener {

    fun didReceivedLocation(location: Location)

    fun locationUpdationFailure(message: String)
}
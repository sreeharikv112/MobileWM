package com.wmapp.ui.splash.views.actions

/**
 * Call backs for activity life cycle and permission requests.
 */
interface ILocationTriggers {

    fun actionStart()
    fun actionPause()
    fun actionActivityResultOK()
    fun actionActivityResultCancelled()
    fun actionRequestPermissionResult(grantResults: IntArray)
}
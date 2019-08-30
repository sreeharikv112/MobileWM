package com.wmapp.ui.splash.views.actions

interface ILocationTriggers {

    fun actionStart()
    fun actionPause()
    fun actionActivityResultOK()
    fun actionActivityResultCancelled()
    fun actionRequestPermissionResult(grantResults: IntArray)
}
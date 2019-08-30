package com.wmapp.app

import android.app.Application
import com.wmapp.dipinjections.components.ApplicationComponent
import com.wmapp.dipinjections.components.DaggerApplicationComponent
import com.wmapp.dipinjections.modules.ApplicationModule
import com.wmapp.dipinjections.modules.NetworkModule

/**
 * Application class for WM Mobile.
 * Handles creation of Dagger Main components.
 * Handles dagger sub component with multiple dependencies
 *
 */
class WMApplication : Application() {

    companion object {
        var mApplicationComponent: ApplicationComponent? = null
        lateinit var instance: WMApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initialiseDaggerComponent()
    }

    /**
     * Returns dagger component
     */
    fun getApplicationComponent():ApplicationComponent?{
        if (mApplicationComponent == null) {
            initialiseDaggerComponent()
        }
        return mApplicationComponent
    }

    /**
     * initialise dagger component
     */
    private fun initialiseDaggerComponent() {

        mApplicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .networkModule(NetworkModule( ))
            .build()
    }

}
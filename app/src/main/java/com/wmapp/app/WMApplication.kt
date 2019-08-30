package com.wmapp.app

import android.app.Application
import com.wmapp.dipinjections.components.ApplicationComponent
import com.wmapp.dipinjections.components.DaggerApplicationComponent
import com.wmapp.dipinjections.modules.ApplicationModule
import com.wmapp.dipinjections.modules.NetworkModule

class WMApplication : Application() {

    companion object {
        lateinit var mApplicationComponent: ApplicationComponent
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
    fun getApplicationComponent():ApplicationComponent{
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
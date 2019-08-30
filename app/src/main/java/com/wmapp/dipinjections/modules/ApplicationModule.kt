package com.wmapp.dipinjections.modules

import android.app.Application
import com.wmapp.common.AppConstants
import com.wmapp.common.AppUtils
import com.wmapp.dipinjections.customscope.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(var application: Application) {

    @Provides
    @ApplicationScope
    fun application(): Application {
        return  application
    }

    @Provides
    @ApplicationScope
    fun appConstants(): AppConstants {
        return  AppConstants()
    }

    @Provides
    @ApplicationScope
    fun getUtils(): AppUtils {
        return  AppUtils(application)
    }

}
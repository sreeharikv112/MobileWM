package com.wmapp.ui.splash.views

import android.content.Intent
import android.os.Bundle
import com.wmapp.R
import com.wmapp.common.AppConstants
import com.wmapp.common.AppUtils
import com.wmapp.ui.base.views.BaseActivity
import com.wmapp.ui.home.views.HomeActivity
import javax.inject.Inject

class MainActivity : BaseActivity(){

    @Inject lateinit var mAppUtils : AppUtils
    @Inject lateinit var mAppConstants : AppConstants
    lateinit var mActivityImpl : MainActivityImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        getInjectionComponent().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initiateDataProcess()
    }

    override fun initiateDataProcess() {
        mActivityImpl = MainActivityImpl(this,mAppUtils)
    }
}

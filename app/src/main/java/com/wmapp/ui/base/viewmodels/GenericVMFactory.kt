package com.wmapp.ui.base.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.wmapp.common.AppUtils
import com.wmapp.networking.NetworkProcessor
import com.wmapp.ui.cardetail.viewmodels.CarDetailViewModel
import com.wmapp.ui.cardetail.views.CarDetailsActivity




class GenericVMFactory(private val mApplication: Application, private val mParams: Array<Any>) :
    ViewModel(), ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass == CarDetailViewModel::class.java) {

            val activityInstance = mParams[0] as CarDetailsActivity
            val networkProcessor = mParams[1] as NetworkProcessor
            val carId = mParams[2] as Int
            val appUtils = mParams[3] as AppUtils

            return CarDetailViewModel(activityInstance, networkProcessor, carId, appUtils) as T
        } else {
            throw kotlin.NullPointerException()
        }
    }
}

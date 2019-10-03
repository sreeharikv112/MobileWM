package com.wmapp.ui.base.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wmapp.common.AppUtils
import com.wmapp.networking.NetworkProcessor
import com.wmapp.ui.cardetail.viewmodels.CarDetailViewModel
import com.wmapp.ui.home.viewmodels.CarsFeedViewModel


class GenericVMFactory(val mParams: Array<Any>) :
    ViewModel(), ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        when (modelClass) {
            CarDetailViewModel::class.java -> {

                val networkProcessor = mParams[0] as NetworkProcessor
                val appUtils = mParams[1] as AppUtils
                val carID = mParams[2] as Int

                return CarDetailViewModel(networkProcessor, appUtils,carID) as T
            }
            CarsFeedViewModel::class.java -> {

                val networkProcessor = mParams[0] as NetworkProcessor
                val appUtils = mParams[1] as AppUtils

                return CarsFeedViewModel(networkProcessor, appUtils) as T
            }
            else -> throw kotlin.ClassCastException()
        }
    }
}

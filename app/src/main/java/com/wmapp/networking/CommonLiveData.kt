package com.wmapp.networking

import androidx.lifecycle.MutableLiveData

/**
 * Common Live data to handle multiple responses.
 */
class CommonLiveData<T>: MutableLiveData<CommonDataWrapper<T>>() {

    fun postSuccess(successData: T){
        postValue(CommonDataWrapper<T>().success(successData))
    }

    fun postNetworkError(errorData: Throwable){
        postValue(CommonDataWrapper<T>().networkError(errorData))
    }

    fun endPointError(errorData: Throwable){
        postValue(CommonDataWrapper<T>().error(errorData))
    }
}
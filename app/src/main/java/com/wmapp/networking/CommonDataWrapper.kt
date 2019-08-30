package com.wmapp.networking

/**
 * Common data wrapper class for handling multiple responses.
 */
class CommonDataWrapper<T> {
    var dataStatus : DataStatus
    var data: T?
    var error: Throwable?

    init {
        dataStatus = DataStatus.END_POINT_ERROR
        data = null
        error = null
    }

    fun success(successData: T): CommonDataWrapper<T>{

        dataStatus = DataStatus.SUCCESS
        data = successData
        error = null
        return this
    }

    fun networkError(errorData: Throwable): CommonDataWrapper<T>{

        dataStatus = DataStatus.NETWORK_ERROR
        data = null
        error = errorData

        return this
    }

    fun error(errorData: Throwable): CommonDataWrapper<T>{

        dataStatus = DataStatus.END_POINT_ERROR
        data = null
        error = errorData

        return this
    }

}
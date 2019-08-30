package com.wmapp.common

import android.content.Context
import android.net.ConnectivityManager

class AppUtils(var context: Context) {



    /**
     * Checks for network
     */
    fun isNetworkConnected(): Boolean {
        var status = false
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = manager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting) {
            status = true
        }
        return status
    }
}
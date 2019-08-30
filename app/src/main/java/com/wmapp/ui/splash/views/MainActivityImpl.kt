package com.wmapp.ui.splash.views

import android.content.DialogInterface
import android.content.Intent
import android.os.Handler
import com.wmapp.R
import com.wmapp.common.AppUtils
import com.wmapp.ui.home.views.HomeActivity


class MainActivityImpl(var context: MainActivity, var appUtils: AppUtils)  {

    private var mDelay : Long = 1000
    private lateinit var mHandler : Handler

    init {
        mHandler= Handler()
        mHandler.postDelayed({
            checkNetwork()
        },mDelay)
    }

    private fun checkNetwork(){
        if(appUtils.isNetworkConnected()){

            context.startActivity(Intent(context, HomeActivity::class.java))
            context.finish()
        }else{
            showAlertDialog()
        }
    }

    private fun showAlertDialog(){
        context.showAlert(
            R.string.lost_connection, R.string.retry, R.string.cancel,
            DialogInterface.OnClickListener { dialog, which ->
                checkNetwork()
            },
            DialogInterface.OnClickListener { dialog, which ->
                context.showToast(context.getString(R.string.app_will_quit))
                context.finishAffinity()
            })
    }
}
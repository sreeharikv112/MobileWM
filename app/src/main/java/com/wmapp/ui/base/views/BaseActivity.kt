package com.wmapp.ui.base.views

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Gravity
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wmapp.R
import com.wmapp.app.WMApplication
import com.wmapp.dipinjections.components.InjectionSubComponent
import com.wmapp.ui.base.components.AlertCallBack
import com.wmapp.ui.base.components.AppLogger

/**
 * Base activity class for providing required dependencies.
 * Helps to inject dagger main component for sub classes.
 * Handles alert messages and call backs.
 * Provides common logger methods.
 * Handles image loading with Glide.
 */
abstract class BaseActivity : AppCompatActivity(), AppLogger , AlertCallBack {

    private var mCallBackAlertDialog: AlertDialog? = null
    private var mIsInjectionComponentUsed: Boolean=false

    fun getInjectionComponent(): InjectionSubComponent {
        check(!mIsInjectionComponentUsed) { "should not use Injection more than once!" }
        mIsInjectionComponentUsed = true
        return (WMApplication).instance.getApplicationComponent()!!.newInjectionComponent()
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {

        super.onCreate(savedInstanceState, persistentState)

    }

    abstract fun initiateDataProcess()

    fun showAlert(message :Int,positiveBtnText: Int){
        MaterialAlertDialogBuilder(this)
            .setMessage(message)
            .setPositiveButton(positiveBtnText, null)
            .setCancelable(false)
            .show()
    }

    fun showAlert(message :String,positiveBtnText: Int,
                  positiveListener: DialogInterface.OnClickListener
                  ){
        MaterialAlertDialogBuilder(this)
            .setMessage(message)
            .setPositiveButton(positiveBtnText, positiveListener)
            .setCancelable(false)
            .show()
    }

    fun showAlert(message :Int, positiveBtnText: Int, negativeBtnText:Int,
                  positiveListener: DialogInterface.OnClickListener,
                  negativeListener: DialogInterface.OnClickListener
    ) {
        MaterialAlertDialogBuilder(this)
            .setMessage(message)
            .setPositiveButton(positiveBtnText, positiveListener)
            .setNegativeButton(negativeBtnText, negativeListener)
            .setCancelable(false)
            .show()
    }

    fun showToast(msg: String) {
        runOnUiThread {
            val toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }

    fun loadImageURL(context: Context, imageView: ImageView, imageURL: String) {
        Glide.with(context).load(imageURL)
            .fallback(android.R.drawable.stat_notify_error)
            .timeout(4500)
            .into(imageView)
    }

    override fun negativeAlertCallBack() {
        mCallBackAlertDialog!!.dismiss()
    }

    override fun positiveAlertCallBack() {
        mCallBackAlertDialog!!.dismiss()
    }

    override fun logD(tag: String, message: String) {
        Log.d(tag,message)
    }

    override fun logE(tag: String, message: String) {
        Log.e(tag,message)
    }

    override fun logV(tag: String, message: String) {
        Log.v(tag,message)
    }

    override fun logI(tag: String, message: String) {
        Log.i(tag,message)
    }
}
package com.wmapp.ui.cardetail.views

import android.os.Bundle
import android.view.View
import com.wmapp.R
import com.wmapp.common.AppConstants.Companion.CARID
import com.wmapp.common.AppUtils
import com.wmapp.networking.NetworkProcessor
import com.wmapp.ui.base.views.BaseActivity
import com.wmapp.ui.home.views.HomeActivity
import kotlinx.android.synthetic.main.activity_car_details.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_cardetails.*
import javax.inject.Inject

/**
 * Represents details of car with attributes.
 * Initiates network call. Processes data with image.
 *
 */
class CarDetailsActivity : BaseActivity(){

    @Inject
    lateinit var mAppUtils: AppUtils
    @Inject
    lateinit var mNetwork: NetworkProcessor
    val mTag = CarDetailsActivity::class.java.simpleName
    lateinit var mCarDetailsImpl: CarDetailsImpl
    var mCarID = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        getInjectionComponent().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_details)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(R.string.car_detail)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        try {
            mCarID = intent.getIntExtra(CARID, -1)
            logD(mTag, "CARID === ${intent.getIntExtra("CARID", -1)}")
        } catch (e: Exception) {
            logE(mTag, "EX == $e")
        }

        initiateDataProcess()
    }

    override fun initiateDataProcess() {
        mCarDetailsImpl = CarDetailsImpl(this, mNetwork, mCarID,mAppUtils)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}

package com.wmapp.ui.cardetail.views

import android.os.Bundle
import android.view.View
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.wmapp.R
import com.wmapp.common.AppConstants.Companion.CARID
import com.wmapp.common.AppUtils
import com.wmapp.networking.DataStatus
import com.wmapp.networking.NetworkProcessor
import com.wmapp.ui.base.viewmodels.GenericVMFactory
import com.wmapp.ui.base.views.BaseActivity
import com.wmapp.ui.cardetail.models.CarDetailGridItem
import com.wmapp.ui.cardetail.models.CarDetails
import com.wmapp.ui.cardetail.viewmodels.CarDetailViewModel
import com.wmapp.ui.home.views.HomeActivity
import com.wmapp.ui.utility.CarDetailAdapter
import kotlinx.android.synthetic.main.activity_car_details.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_cardetails.*
import javax.inject.Inject

/**
 * Represents details of car with attributes.
 * Initiates network call. Processes data with image.
 *
 */
class CarDetailsActivity : BaseActivity(),View.OnClickListener{


    @Inject
    lateinit var mAppUtils: AppUtils
    @Inject
    lateinit var mNetwork: NetworkProcessor
    val mTag = CarDetailsActivity::class.java.simpleName
    lateinit var mCarDetailsImpl: CarDetailsImpl
    private var mCarsDetailVM: CarDetailViewModel? = null
    var mCarID = -1
    lateinit var mProgressBar: ContentLoadingProgressBar
    lateinit var mRecyclerView: RecyclerView
    lateinit var mCardDetailAdapter: CarDetailAdapter
    lateinit var mBookButton: MaterialButton
    private var mIsDataLoaded = false


    override fun onCreate(savedInstanceState: Bundle?) {
        getInjectionComponent().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_details)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(R.string.car_detail)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mProgressBar = findViewById(R.id.progressBar)
        try {
            mCarID = intent.getIntExtra(CARID, -1)
            logD(mTag, "CARID === ${intent.getIntExtra("CARID", -1)}")
        } catch (e: Exception) {
            logE(mTag, "onCreate EX == $e")
        }

        setupRecyclerView()
    }

    fun setupRecyclerView(){

        mRecyclerView = findViewById(R.id.carDetailsRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mCardDetailAdapter = CarDetailAdapter(this, ArrayList())
        mRecyclerView.adapter = mCardDetailAdapter

        mBookButton = findViewById(R.id.btnQuickRent)
        mBookButton.visibility = View.INVISIBLE

        initiateDataProcess()
    }

    override fun initiateDataProcess() {
        var array = arrayOf(this,mNetwork,mCarID,mAppUtils)
        mCarsDetailVM = ViewModelProviders.of(this , GenericVMFactory(application,array)).get(CarDetailViewModel::class.java)

        //Listen to list updates.
        mCarsDetailVM!!.getCarDetails(mCarID, mNetwork).observe(this, Observer { data ->
            mProgressBar.visibility = View.INVISIBLE
            when (data.dataStatus) {
                DataStatus.SUCCESS -> {

                    try {
                        val inputData = data.data as CarDetails
                        val lisItem = ArrayList<CarDetailGridItem>()
                        mBookButton.visibility = View.VISIBLE
                        mBookButton.setOnClickListener(this)
                        mIsDataLoaded=true
                        mCarsDetailVM!!.populateCarDetail(this,mCardDetailAdapter,lisItem, inputData)
                    } catch (e: Exception) {
                        showToast("Sorry! Not able to find details of car")
                        logE(mTag, "initiateDataProcess EX === ${e.toString()}")
                    }
                }
                DataStatus.NETWORK_ERROR -> {
                    logD(mTag, "NETWORK_ERROR")
                    showAlert(R.string.lost_connection, R.string.ok)
                }
                DataStatus.END_POINT_ERROR -> {
                    logD(mTag, "END_POINT_ERROR")
                    showAlert(R.string.data_processing_error, R.string.ok)
                }
            }
        })
    }

    override fun onClick(v: View?) {
        if (v!!.id == R.id.btnQuickRent) {
            if (mIsDataLoaded && mCarID != -1) {
                if(mAppUtils.isNetworkConnected()) {
                    showToast(getString(R.string.car_booked_simulation))
                    //Commented out the implementation for now
                    //bookCar()

                }else{
                    showToast(getString(R.string.lost_connection))
                }

            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}

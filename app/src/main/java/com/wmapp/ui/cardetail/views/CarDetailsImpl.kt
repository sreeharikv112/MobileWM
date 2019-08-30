package com.wmapp.ui.cardetail.views

import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.button.MaterialButton
import com.wmapp.R
import com.wmapp.networking.DataStatus
import com.wmapp.networking.NetworkProcessor
import com.wmapp.ui.cardetail.models.BookedResponse
import com.wmapp.ui.cardetail.models.CarDetailGridItem
import com.wmapp.ui.cardetail.models.CarDetails
import com.wmapp.ui.cardetail.viewmodels.CarDetailViewModel
import com.wmapp.ui.utility.CarDetailAdapter

/**
 * Handles car details functional flow.
 * Initiates rest call, listens to the data update using ViewModel.
 * Handles car booking request.
 */
class CarDetailsImpl(
    val context: CarDetailsActivity,
    val mNetwork: NetworkProcessor,
    val carId: Int
) : View.OnClickListener {

    private var mCarsDetailVM: CarDetailViewModel? = null
    private val mTag = CarDetailsImpl::class.java.simpleName
    private val mProgressBar: ContentLoadingProgressBar
    private var mRecyclerView: RecyclerView
    private var mCardDetailAdapter: CarDetailAdapter
    private var mCarDetailVM: CarDetailViewModel
    private var mBookButton: MaterialButton
    var mImageView: ImageView
    var mIsDataLoaded = false
    var mCurrentCarID = -1

    init {
        context.logD(mTag, "CarDetailsImpl init")
        mCarsDetailVM = ViewModelProviders.of(context).get(CarDetailViewModel::class.java)
        mImageView = context.findViewById(R.id.carImageView)
        mProgressBar = context.findViewById(R.id.progressBar)
        mProgressBar.visibility = View.VISIBLE
        mBookButton = context.findViewById(R.id.btnQuickRent)
        mBookButton.visibility = View.INVISIBLE
        mRecyclerView = context.findViewById(R.id.carDetailsRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mCardDetailAdapter = CarDetailAdapter(context, ArrayList())
        mRecyclerView.adapter = mCardDetailAdapter
        mCarDetailVM = ViewModelProviders.of(context).get(CarDetailViewModel::class.java)

        retrieveData()
    }

    private fun retrieveData() {
        context.logD(mTag, "retrieveData")
        mCarsDetailVM!!.getCarDetails(carId, mNetwork).observe(context, Observer { data ->

            mProgressBar.visibility = View.INVISIBLE
            when (data.dataStatus) {
                DataStatus.SUCCESS -> {

                    try {
                        val inputData = data.data as CarDetails
                        val lisItem = ArrayList<CarDetailGridItem>()
                        populateCarDetail(lisItem, inputData)
                    } catch (e: Exception) {
                        context.showToast("Sorry! Not able to find details of car")
                        context.logE(mTag, "EX === ${e.toString()}")
                    }
                }
                DataStatus.NETWORK_ERROR -> {
                    context.logD(mTag, "NETWORK_ERROR")
                    context.showAlert(R.string.lost_connection, R.string.ok)
                }
                DataStatus.END_POINT_ERROR -> {
                    context.logD(mTag, "END_POINT_ERROR")
                    context.showAlert(R.string.data_processing_error, R.string.ok)
                }
            }
        })
    }

    private fun bookCar() {
        mProgressBar.visibility = View.VISIBLE
        context.logD(mTag, "retrieveData")
        mCarsDetailVM!!.bookCarDetails(carId, mNetwork).observe(context, Observer { data ->
            mProgressBar.visibility = View.INVISIBLE
            when (data.dataStatus) {

                DataStatus.SUCCESS -> {
                    try {
                        val carBookedDetails = data.data as BookedResponse
                        val carReservationID = carBookedDetails.reservationId
                        val startAddress = carBookedDetails.startAddress

                        context.showAlert("Vehicle Booked with Reservation $carReservationID and start address " +
                                "" +
                                "is $startAddress", R.string.ok,
                            DialogInterface.OnClickListener { dialog, which ->
                                context.onSupportNavigateUp()
                            }
                        )

                    } catch (e: Exception) {
                        context.showToast("Sorry! Not able to process booking of vehicle")
                        context.logE(mTag, "EX === ${e.toString()}")
                    }
                }
                DataStatus.NETWORK_ERROR -> {
                    context.logD(mTag, "NETWORK_ERROR")
                    context.showAlert(R.string.lost_connection, R.string.ok)
                }
                DataStatus.END_POINT_ERROR -> {
                    context.logD(mTag, "END_POINT_ERROR")
                    context.showAlert(R.string.data_processing_error, R.string.ok)
                }
            }
        })
    }

    private fun populateCarDetail(lisItem: ArrayList<CarDetailGridItem>, inputData: CarDetails) {
        lisItem.add(
            CarDetailGridItem(
                "Title",
                if (TextUtils.isEmpty(inputData.title)) " " else inputData.title
            )
        )
        lisItem.add(
            CarDetailGridItem(
                context.getString(R.string.licence_plate),
                inputData.licencePlate
            )
        )
        lisItem.add(
            CarDetailGridItem(
                context.getString(R.string.vehicle_state_id),
                inputData.vehicleStateId.toString()
            )
        )
        lisItem.add(
            CarDetailGridItem(
                context.getString(R.string.hardware_id),
                inputData.hardwareId
            )
        )
        lisItem.add(
            CarDetailGridItem(
                context.getString(R.string.pricing_time),
                inputData.pricingTime
            )
        )
        lisItem.add(
            CarDetailGridItem(
                context.getString(R.string.pricing_parking),
                inputData.pricingParking
            )
        )
        lisItem.add(CarDetailGridItem(context.getString(R.string.address), inputData.address))
        lisItem.add(CarDetailGridItem(context.getString(R.string.zipcode), inputData.zipCode))
        lisItem.add(CarDetailGridItem(context.getString(R.string.city), inputData.city))
        lisItem.add(CarDetailGridItem(context.getString(R.string.address), inputData.address))
        lisItem.add(
            CarDetailGridItem(
                "Reservation State",
                if (inputData.reservationState == 0) "Not Reserved" else "Reserved"
            )
        )
        lisItem.add(
            CarDetailGridItem(
                context.getString(R.string.damage_desc),
                inputData.damageDescription
            )
        )
        lisItem.add(
            CarDetailGridItem(
                context.getString(R.string.is_clean),
                if (inputData.isClean) "YES" else "NO"
            )
        )
        lisItem.add(
            CarDetailGridItem(
                context.getString(R.string.is_damaged),
                if (inputData.isDamaged) "YES" else "NO"
            )
        )
        lisItem.add(
            CarDetailGridItem(
                "Is Activated By Hardware",
                if (inputData.isActivatedByHardware) "YES" else "NO"
            )
        )
        mCardDetailAdapter.addListItems(lisItem)
        mIsDataLoaded = true
        mCurrentCarID = inputData.carId
        mBookButton.visibility = View.VISIBLE
        mBookButton.setOnClickListener(this)
        context.loadImageURL(context,mImageView,inputData.vehicleTypeImageUrl)
    }

    override fun onClick(v: View?) {
        if (v!!.id == R.id.btnQuickRent) {
            if (mIsDataLoaded && mCurrentCarID != -1) {
                bookCar()
            }
        }
    }

}
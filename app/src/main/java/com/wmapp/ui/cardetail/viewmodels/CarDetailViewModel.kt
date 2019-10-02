package com.wmapp.ui.cardetail.viewmodels

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.wmapp.R
import com.wmapp.common.AppConstants.Companion.CAR_BOOK_REQ
import com.wmapp.common.AppConstants.Companion.CAR_DETAILS_REQ
import com.wmapp.common.AppUtils
import com.wmapp.networking.CommonLiveData
import com.wmapp.networking.DataStatus
import com.wmapp.networking.NetworkProcessor
import com.wmapp.ui.cardetail.models.BookCar
import com.wmapp.ui.cardetail.models.BookedResponse
import com.wmapp.ui.cardetail.models.CarDetailGridItem
import com.wmapp.ui.cardetail.models.CarDetails
import com.wmapp.ui.cardetail.views.CarDetailsActivity
import com.wmapp.ui.utility.CarDetailAdapter

/**
 * View model for car details screen.
 */
class CarDetailViewModel(
    val mContext: CarDetailsActivity,
    val mNetwork: NetworkProcessor,
    val carId: Int,
    val appUtils: AppUtils

) : ViewModel() {

    var mCarDetailResponse: CommonLiveData<CarDetails>? = null
    var mBookedResponse: CommonLiveData<BookedResponse>? = null
    lateinit var mImageView: ImageView
    val mTag = CarDetailViewModel::class.java.simpleName

    init {

    }
    /**
     * Retrieves response from api for specific car.
     */
    fun getCarDetails(carId: Int, networkProcessor: NetworkProcessor): CommonLiveData<CarDetails> {
        if (null == mCarDetailResponse) {
            mCarDetailResponse = networkProcessor.getGenericRemoteData(CAR_DETAILS_REQ, carId, null)
                    as CommonLiveData<CarDetails>
        }
        return mCarDetailResponse as CommonLiveData<CarDetails>
    }

    /**
     * Makes booking request with specific car
     */
    fun bookCarDetails(
        carId: Int,
        networkProcessor: NetworkProcessor
    ): CommonLiveData<BookedResponse> {
        if (null == mBookedResponse) {
            val bookCar = BookCar(carId)
            mBookedResponse = networkProcessor.getGenericRemoteData(CAR_BOOK_REQ, -1, bookCar)
                    as CommonLiveData<BookedResponse>
        }
        return mBookedResponse as CommonLiveData<BookedResponse>
    }

    /**
     * Renders car details in recycler view.
     */
    fun populateCarDetail(context: CarDetailsActivity,cardDetailAdapter: CarDetailAdapter,lisItem: ArrayList<CarDetailGridItem>, inputData: CarDetails) {
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
        cardDetailAdapter.addListItems(lisItem)
        mImageView = context.findViewById(R.id.carImageView)
        context.loadImageURL(context,mImageView,inputData.vehicleTypeImageUrl)
    }

}
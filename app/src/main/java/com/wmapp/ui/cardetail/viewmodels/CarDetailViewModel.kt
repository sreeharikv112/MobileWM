package com.wmapp.ui.cardetail.viewmodels

import android.text.TextUtils
import androidx.lifecycle.ViewModel
import com.wmapp.common.AppConstants.Companion.CAR_BOOK_REQ
import com.wmapp.common.AppConstants.Companion.CAR_DETAILS_REQ
import com.wmapp.common.AppUtils
import com.wmapp.networking.CommonLiveData
import com.wmapp.networking.NetworkProcessor
import com.wmapp.ui.cardetail.models.BookCar
import com.wmapp.ui.cardetail.models.BookedResponse
import com.wmapp.ui.cardetail.models.CarDetailGridItem
import com.wmapp.ui.cardetail.models.CarDetails
import com.wmapp.ui.utility.CarDetailAdapter

/**
 * View model for car details screen.
 */
class CarDetailViewModel(
    val mNetworkProcessor: NetworkProcessor,
    val appUtils: AppUtils,
    val carId: Int

) : ViewModel() {

    var mCarDetailResponse: CommonLiveData<CarDetails>? = null
    var mBookedResponse: CommonLiveData<BookedResponse>? = null


    /**
     * Retrieves response from api for specific car.
     */
    fun getCarDetails(): CommonLiveData<CarDetails> {
        if (null == mCarDetailResponse) {
            mCarDetailResponse = mNetworkProcessor.getGenericRemoteData(CAR_DETAILS_REQ, carId, null)
                    as CommonLiveData<CarDetails>
        }
        return mCarDetailResponse as CommonLiveData<CarDetails>
    }

    /**
     * Makes booking request with specific car.
     * Commented out for now. Will show only toast message to user.
     */
    fun bookCarDetails(): CommonLiveData<BookedResponse> {
        if (null == mBookedResponse) {
            val bookCar = BookCar(carId)
            mBookedResponse = mNetworkProcessor.getGenericRemoteData(CAR_BOOK_REQ, -1, bookCar)
                    as CommonLiveData<BookedResponse>
        }
        return mBookedResponse as CommonLiveData<BookedResponse>
    }

    /**
     * Renders car details in recycler view.
     */
    fun populateCarDetail(listPlaceholder: MutableList<String>, cardDetailAdapter: CarDetailAdapter, lisItem: ArrayList<CarDetailGridItem>, inputData: CarDetails) {
        lisItem.add(
            CarDetailGridItem(
                listPlaceholder[0],
                if (TextUtils.isEmpty(inputData.title)) " " else inputData.title
            )
        )
        lisItem.add(
            CarDetailGridItem(
            listPlaceholder[1],
                inputData.licencePlate
            )
        )
        lisItem.add(
            CarDetailGridItem(
                listPlaceholder[2],
                inputData.vehicleStateId.toString()
            )
        )
        lisItem.add(
            CarDetailGridItem(
                listPlaceholder[3],
                inputData.hardwareId
            )
        )
        lisItem.add(
            CarDetailGridItem(
                listPlaceholder[4],
                inputData.pricingTime
            )
        )
        lisItem.add(
            CarDetailGridItem(
                listPlaceholder[5],
                inputData.pricingParking
            )
        )
        lisItem.add(CarDetailGridItem(listPlaceholder[6], inputData.address))
        lisItem.add(CarDetailGridItem(listPlaceholder[7], inputData.zipCode))
        lisItem.add(CarDetailGridItem(listPlaceholder[8], inputData.city))
        lisItem.add(CarDetailGridItem(listPlaceholder[9], inputData.address))
        lisItem.add(
            CarDetailGridItem(
                listPlaceholder[10],
                if (inputData.reservationState == 0) "Not Reserved" else "Reserved"
            )
        )
        lisItem.add(
            CarDetailGridItem(
                listPlaceholder[11],
                inputData.damageDescription
            )
        )
        lisItem.add(
            CarDetailGridItem(
                listPlaceholder[12],
                if (inputData.isClean) "YES" else "NO"
            )
        )
        lisItem.add(
            CarDetailGridItem(
                listPlaceholder[13],
                if (inputData.isDamaged) "YES" else "NO"
            )
        )
        lisItem.add(
            CarDetailGridItem(
                listPlaceholder[14],
                if (inputData.isActivatedByHardware) "YES" else "NO"
            )
        )
        cardDetailAdapter.addListItems(lisItem)
    }

}
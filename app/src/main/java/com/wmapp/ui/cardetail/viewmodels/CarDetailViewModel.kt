package com.wmapp.ui.cardetail.viewmodels

import androidx.lifecycle.ViewModel
import com.wmapp.common.AppConstants.Companion.CAR_BOOK_REQ
import com.wmapp.common.AppConstants.Companion.CAR_DETAILS_REQ
import com.wmapp.networking.CommonLiveData
import com.wmapp.networking.NetworkProcessor
import com.wmapp.ui.cardetail.models.BookCar
import com.wmapp.ui.cardetail.models.BookedResponse
import com.wmapp.ui.cardetail.models.CarDetails

/**
 * View model for car details screen.
 */
class CarDetailViewModel : ViewModel() {

    var mCarDetailResponse: CommonLiveData<CarDetails>? = null
    var mBookedResponse: CommonLiveData<BookedResponse>? = null

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


}
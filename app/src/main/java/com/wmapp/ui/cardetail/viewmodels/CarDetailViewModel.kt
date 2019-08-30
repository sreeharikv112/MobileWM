package com.wmapp.ui.cardetail.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.wmapp.networking.CommonLiveData
import com.wmapp.networking.NetworkProcessor
import com.wmapp.ui.cardetail.models.BookCar
import com.wmapp.ui.cardetail.models.BookedResponse
import com.wmapp.ui.cardetail.models.CarDetails

class CarDetailViewModel : ViewModel() {

    var mCarDetailResponse : CommonLiveData<CarDetails>? = null
    var mBookedResponse: CommonLiveData<BookedResponse>? = null

    val mTag = CarDetailViewModel::class.java.simpleName

    fun getCarDetails(carId : Int,networkProcessor: NetworkProcessor) : CommonLiveData<CarDetails>{
        if(null == mCarDetailResponse){
            mCarDetailResponse = networkProcessor.getCarDetailsData(carId)
        }
        return mCarDetailResponse as CommonLiveData<CarDetails>
    }

    fun bookCarDetails(carId : Int,networkProcessor: NetworkProcessor): CommonLiveData<BookedResponse>{
        Log.d(mTag,"bookCarDetails")
        if(null == mBookedResponse){

            val bookCar = BookCar(carId)
            mBookedResponse = networkProcessor.bookCarDetailsData(bookCar)
        }
        return mBookedResponse as CommonLiveData<BookedResponse>
    }


}
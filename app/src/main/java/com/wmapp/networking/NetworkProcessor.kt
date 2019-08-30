package com.wmapp.networking

import android.util.Log
import com.wmapp.common.AppConstants
import com.wmapp.ui.cardetail.models.BookCar
import com.wmapp.ui.cardetail.models.BookedResponse
import com.wmapp.ui.cardetail.models.CarDetails
import com.wmapp.ui.home.models.CarsFeed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

/**
 * Handles multiple network requests.
 * Uses coroutines.
 * Coroutines can be replaced with RxAndroid as well.
 */
class NetworkProcessor (var networkService: NetworkService ){

    val mTag = NetworkProcessor::class.java.simpleName


    fun getRemoteData() : CommonLiveData<ArrayList<CarsFeed>> {

        var responseData: CommonLiveData<ArrayList<CarsFeed>> =
            CommonLiveData()

        CoroutineScope(Dispatchers.IO).launch {

            val response = networkService.getAllCarsFeed()

            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        responseData.postSuccess(response.body()!!)
                    } else {
                        responseData.endPointError(NetworkProcessingError("Unable to process data"))
                    }
                } catch (e: HttpException) {
                    Log.d(mTag, "NetworkProcessor loadMatches() HttpException!!! ${e.toString()}")
                    responseData.postNetworkError(NetworkProcessingError(e.message()))
                } catch (e: Throwable) {
                    Log.d(mTag, "NetworkProcessor loadMatches() Throwable!!! ${e.toString()}")
                    responseData.postNetworkError(NetworkProcessingError(e.toString()))
                }
            }
        }
        return responseData
    }

    fun getCarDetailsData(carId: Int) : CommonLiveData<CarDetails> {

        var responseData: CommonLiveData<CarDetails> =
            CommonLiveData()

        CoroutineScope(Dispatchers.IO).launch {

            val response = networkService.getCarsDetails(carId)

            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        responseData.postSuccess(response.body()!!)
                    } else {
                        responseData.endPointError(NetworkProcessingError("Unable to process data"))
                    }
                } catch (e: HttpException) {
                    Log.d(mTag, "NetworkProcessor loadMatches() HttpException!!! ${e.toString()}")
                    responseData.postNetworkError(NetworkProcessingError(e.message()))
                } catch (e: Throwable) {
                    Log.d(mTag, "NetworkProcessor loadMatches() Throwable!!! ${e.toString()}")
                    responseData.postNetworkError(NetworkProcessingError(e.toString()))
                }
            }
        }
        return responseData
    }

    fun bookCarDetailsData(bookCar: BookCar) : CommonLiveData<BookedResponse> {

        var responseData: CommonLiveData<BookedResponse> =
            CommonLiveData()

        CoroutineScope(Dispatchers.IO).launch {

            val response = networkService.bookCarRequest(AppConstants.BOOK_URL,
                AppConstants.AUTHORIZATION,
                bookCar
                )

            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        responseData.postSuccess(response.body()!!)
                    } else {
                        responseData.endPointError(NetworkProcessingError("Unable to process data"))
                    }
                } catch (e: HttpException) {
                    Log.d(mTag, "NetworkProcessor loadMatches() HttpException!!! ${e.toString()}")
                    responseData.postNetworkError(NetworkProcessingError(e.message()))
                } catch (e: Throwable) {
                    Log.d(mTag, "NetworkProcessor loadMatches() Throwable!!! ${e.toString()}")
                    responseData.postNetworkError(NetworkProcessingError(e.toString()))
                }
            }
        }
        return responseData
    }
}
package com.wmapp.networking

import com.wmapp.common.AppConstants
import com.wmapp.common.AppConstants.Companion.CAR_BOOK_REQ
import com.wmapp.common.AppConstants.Companion.CAR_DETAILS_REQ
import com.wmapp.common.AppConstants.Companion.CAR_FEED_REQ
import com.wmapp.ui.cardetail.models.BookCar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response

/**
 * Handles multiple network requests.
 * Uses coroutines.
 * All requests and responses are handled in generic way.
 * Differentiated by constants and manipulated response accordingly.
 */
class NetworkProcessor(var networkService: NetworkService) {

    lateinit var response: Response<*>

    fun getGenericRemoteData(reqNum: Int, carID: Int, bookCar: BookCar?): CommonLiveData<Any> {

        var responseData: CommonLiveData<Any> = CommonLiveData()

        CoroutineScope(Dispatchers.IO).launch {
            when (reqNum) {
                CAR_FEED_REQ ->
                    response = networkService.getAllCarsFeed()
                CAR_DETAILS_REQ ->
                    //response = networkService.getCarsDetails(carID)
                    response = networkService.getCarsDetails()
                CAR_BOOK_REQ ->
                    response = networkService.bookCarRequest(
                        AppConstants.BOOK_URL,
                        AppConstants.AUTHORIZATION,
                        bookCar!!
                    )
            }
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        responseData.postSuccess(response.body()!!)
                    } else {
                        responseData.endPointError(NetworkProcessingError("Unable to process data"))
                    }
                } catch (e: HttpException) {
                    responseData.postNetworkError(NetworkProcessingError(e.message()))
                } catch (e: Throwable) {
                    responseData.postNetworkError(NetworkProcessingError(e.toString()))
                }
            }
        }
        return responseData
    }
}
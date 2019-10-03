package com.wmapp.ui.home.viewmodels

import androidx.lifecycle.ViewModel
import com.wmapp.common.AppConstants
import com.wmapp.common.AppUtils
import com.wmapp.networking.CommonLiveData
import com.wmapp.networking.NetworkProcessor
import com.wmapp.ui.home.models.CarsFeed

/**
 * View model for cars details.
 * Requests network data for getting all cars details.
 */
class CarsFeedViewModel(
    val networkProcessor : NetworkProcessor,
    val appUtils: AppUtils
    ): ViewModel() {

    var mCarFeedData: CommonLiveData<ArrayList<CarsFeed>>? = null

    fun getCarsFeedData(): CommonLiveData<ArrayList<CarsFeed>> {
        if (null == mCarFeedData) {
            mCarFeedData =
                networkProcessor.getGenericRemoteData(AppConstants.CAR_FEED_REQ, -1, null)
                        as CommonLiveData<ArrayList<CarsFeed>>
        }
        return mCarFeedData as CommonLiveData<ArrayList<CarsFeed>>
    }
}
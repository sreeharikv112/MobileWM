package com.wmapp.ui.home.viewmodels

import androidx.lifecycle.ViewModel
import com.wmapp.networking.CommonLiveData
import com.wmapp.networking.NetworkProcessor
import com.wmapp.ui.home.models.CarsFeed

class CarsFeedViewModel : ViewModel() {

    var mCardFeedData : CommonLiveData<ArrayList<CarsFeed>>? = null

    fun getCarsFeedData(networkProcessor: NetworkProcessor): CommonLiveData<ArrayList<CarsFeed>> {
        if(null == mCardFeedData){
            mCardFeedData = networkProcessor.getRemoteData()
        }
        return mCardFeedData as CommonLiveData<ArrayList<CarsFeed>>
    }
}
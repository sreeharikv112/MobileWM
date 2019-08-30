package com.wmapp.ui.utility

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class POIClusterItem(
                    var selectedID: Int,
                    lat: Double, lon:Double,
                     var titleHeader:String,
                     var snippetData: String,
                     var image: Int
                     )
    : ClusterItem {

    var mPosition : LatLng = LatLng(lat,lon)


    override fun getSnippet(): String {
        return snippetData
    }

    override fun getTitle(): String {
        return titleHeader
    }

    override fun getPosition(): LatLng {
        return mPosition
    }

    fun getProfilePhoto(): Int {
        return image
    }

    fun getID(): Int {
        return selectedID
    }
}
package com.wmapp.ui.home.views

import android.content.Intent
import android.text.TextUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.wmapp.R
import com.wmapp.common.AppConstants
import com.wmapp.networking.DataStatus
import com.wmapp.networking.NetworkProcessor
import com.wmapp.ui.cardetail.views.CarDetailsActivity
import com.wmapp.ui.home.models.CarsFeed
import com.wmapp.ui.home.viewmodels.CarsFeedViewModel
import com.wmapp.ui.utility.AssetRenderer
import com.wmapp.ui.utility.POIClusterItem

class HomeActivityImpl (var context : HomeActivity,var mNetwork : NetworkProcessor) :

    ClusterManager.OnClusterClickListener<POIClusterItem>,
    ClusterManager.OnClusterInfoWindowClickListener<POIClusterItem>,
    ClusterManager.OnClusterItemClickListener<POIClusterItem>,
    ClusterManager.OnClusterItemInfoWindowClickListener<POIClusterItem>
{
    private var mCarsFeedVM : CarsFeedViewModel ? = null
    val mTag = HomeActivityImpl::class.java.simpleName
    var mGoogleMap: GoogleMap? = null
    lateinit var mClusterManager:ClusterManager<POIClusterItem>
    var mFocusIndex: Int = -1
    var mCurrentLocMarkerOption : MarkerOptions? = null
    var mLastAddedMarker : Marker? = null
    var mClickedPOI = -1

    init {
        context.logD(mTag,"init")
        mCarsFeedVM = ViewModelProviders.of(context).get(CarsFeedViewModel::class.java)
        retrieveData()
    }

    private fun retrieveData() {

        mCarsFeedVM!!.getCarsFeedData(mNetwork).observe(context, Observer {
                data ->
            //Set shared list data in Base
            when (data.dataStatus){
                DataStatus.SUCCESS -> {
                    context.logD(mTag,"data ===  ${data.data!!}")

                    var listOfCars = data.data as ArrayList<CarsFeed>


                    processClusterData(listOfCars)
                }
                DataStatus.NETWORK_ERROR -> {
                    context.logD(mTag,"NETWORK_ERROR")
                    context.showAlert(R.string.lost_connection, R.string.ok)
                }
                DataStatus.END_POINT_ERROR ->{
                    context.logD(mTag,"END_POINT_ERROR")
                    context.showAlert(R.string.data_processing_error, R.string.ok)
                }
            }
        })
    }

    private fun processClusterData(listOfCars: ArrayList<CarsFeed>) {
        if(null!=mGoogleMap){
            mClusterManager = ClusterManager(context, mGoogleMap)
            mClusterManager.renderer = AssetRenderer(context, mGoogleMap!!, mClusterManager)
            mGoogleMap!!.setOnCameraIdleListener(mClusterManager)
            mGoogleMap!!.setOnMarkerClickListener(mClusterManager)
            mGoogleMap!!.setOnInfoWindowClickListener(mClusterManager)
            mClusterManager.setOnClusterClickListener(this)
            mClusterManager.setOnClusterInfoWindowClickListener(this)
            mClusterManager.setOnClusterItemClickListener(this)
            mClusterManager.setOnClusterItemInfoWindowClickListener(this)

            for(item in listOfCars){

                var lat = item.lat
                var lon = item.lon
                var carID = item.carId

                if(mFocusIndex ==-1){
                    mGoogleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lon), 15f))
                }

                var title = ""
                if(TextUtils.isEmpty(item.title)){

                    if(TextUtils.isEmpty(item.licencePlate))
                        title = item.address
                    else
                        title = item.licencePlate
                }
                else{
                    title = item.title
                }


                var clusterItem = POIClusterItem(carID,lat,lon,title, item.address + " "+
                        item.city, R.mipmap.car_list_2)

                mClusterManager.addItem(clusterItem)
            }
            mClusterManager.cluster()
        }
    }


    fun onActivityMapReady(googleMap: GoogleMap){
        mGoogleMap = googleMap
    }


    override fun onClusterClick(cluster: Cluster<POIClusterItem>?): Boolean {
        val builder = LatLngBounds.builder()
        for (item in cluster!!.items) {
            builder.include(item.position)
        }
        // Get the LatLngBounds
        val bounds = builder.build()
        // Animate camera to the bounds
        try {
            mGoogleMap!!.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }

    override fun onClusterInfoWindowClick(item: Cluster<POIClusterItem>?) {
        context.logD(mTag,"onClusterInfoWindowClick ")
    }

    override fun onClusterItemClick(item: POIClusterItem?): Boolean {
        context.logD(mTag,"onClusterItemClick ${item?.getID()}")
        if(mClickedPOI ==item!!.getID()){
            invokeNewActivity(item?.getID())
        }
        mClickedPOI = item!!.getID()
        return false
    }

    override fun onClusterItemInfoWindowClick(item: POIClusterItem?) {
        context.logD(mTag,"onClusterItemInfoWindowClick ")
        invokeNewActivity(item?.getID())
    }

    private fun invokeNewActivity(carID : Int?){
        val intent = Intent(context, CarDetailsActivity::class.java)
        intent.putExtra(AppConstants.CARID, carID)
        context.startActivity(intent)

    }

    fun showUpdatedLocation(newLoc: LatLng) {
        
        if(mLastAddedMarker != null){
            mLastAddedMarker!!.remove()
        }
        mCurrentLocMarkerOption = MarkerOptions().position(newLoc).title(context.getString(R.string.my_current_location))
        mLastAddedMarker = mGoogleMap!!.addMarker(mCurrentLocMarkerOption)
    }
}
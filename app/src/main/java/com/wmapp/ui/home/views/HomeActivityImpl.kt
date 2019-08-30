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
import com.wmapp.common.AppUtils
import com.wmapp.networking.DataStatus
import com.wmapp.networking.NetworkProcessor
import com.wmapp.ui.cardetail.views.CarDetailsActivity
import com.wmapp.ui.home.models.CarsFeed
import com.wmapp.ui.home.viewmodels.CarsFeedViewModel
import com.wmapp.ui.utility.AssetRenderer
import com.wmapp.ui.utility.POIClusterItem

/**
 * Handles google map POI plotting.
 * Handles users click on POI.
 * Plots user's current live location if available.
 * Navigates to car details screen upon user POI selection.
 */
class HomeActivityImpl (var context : HomeActivity,var mNetwork : NetworkProcessor,var appUtils : AppUtils) :

    ClusterManager.OnClusterClickListener<POIClusterItem>,
    ClusterManager.OnClusterInfoWindowClickListener<POIClusterItem>,
    ClusterManager.OnClusterItemClickListener<POIClusterItem>,
    ClusterManager.OnClusterItemInfoWindowClickListener<POIClusterItem>
{
    private var mCarsFeedVM : CarsFeedViewModel ? = null
    private val mTag = HomeActivityImpl::class.java.simpleName
    private var mGoogleMap: GoogleMap? = null
    private lateinit var mClusterManager:ClusterManager<POIClusterItem>
    private var mFocusIndex: Int = -1
    private var mCurrentLocMarkerOption : MarkerOptions? = null
    private var mLastAddedMarker : Marker? = null
    private var mClickedPOI = -1

    init {
        context.logD(mTag,"init")
        mCarsFeedVM = ViewModelProviders.of(context).get(CarsFeedViewModel::class.java)
        retrieveData()
    }

    /**
     * Initiates REST call for all car's data.
     * Listens to view models update.
     */
    private fun retrieveData() {

        mCarsFeedVM!!.getCarsFeedData(mNetwork).observe(context, Observer {
                data ->
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

    /**
     * Creates POI cluster with received data.
     * Attaches listeners for user's click on POI.
     * Concentrates on specific POI on user click.
     *
     */
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

                val lat = item.lat
                val lon = item.lon

                if(mFocusIndex ==-1){
                    mGoogleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lon), 15f))
                }
                var title = if(TextUtils.isEmpty(item.title)){
                    if(TextUtils.isEmpty(item.licencePlate))
                        item.address
                    else
                        item.licencePlate
                } else{
                    item.title
                }
                val clusterItem = POIClusterItem(item.carId,lat,lon,title, item.address + " "+
                        item.city, R.mipmap.car_list_2)

                mClusterManager.addItem(clusterItem)
            }
            mClusterManager.cluster()
        }
    }

    /**
     * receives Gmap call back from activity
     */
    fun onActivityMapReady(googleMap: GoogleMap){
        mGoogleMap = googleMap
    }

    /**
     * Handles cluster on click by user.
     * Manipulates UI based and focus on cluster on user interaction.
     */
    override fun onClusterClick(cluster: Cluster<POIClusterItem>?): Boolean {
        val builder = LatLngBounds.builder()
        for (item in cluster!!.items) {
            builder.include(item.position)
        }
        val bounds = builder.build()
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

    /**
     * Handles POI cluster click.
     * If clicked twice in same POI, user will be navigated to detailed page.
     */
    override fun onClusterItemClick(item: POIClusterItem?): Boolean {
        context.logD(mTag,"onClusterItemClick ${item?.getID()}")
        if(mClickedPOI ==item!!.getID()){
            invokeNewActivity(item?.getID())
        }
        mClickedPOI = item!!.getID()
        return false
    }

    /**
     * Handles POI info window click.
     * User will be navigated to detailed page.
     */
    override fun onClusterItemInfoWindowClick(item: POIClusterItem?) {
        context.logD(mTag,"onClusterItemInfoWindowClick ")
        invokeNewActivity(item?.getID())
    }

    /**
     * Invokes new activity with selected car id.
     */
    private fun invokeNewActivity(carID : Int?){
        if(appUtils.isNetworkConnected()){
            val intent = Intent(context, CarDetailsActivity::class.java)
            intent.putExtra(AppConstants.CARID, carID)
            context.startActivity(intent)
        }
        else{
            context.showToast(context.getString(R.string.lost_connection))
        }
    }

    /**
     * Updates user's live location with marker.
     */
    fun showUpdatedLocation(newLoc: LatLng) {
        
        if(mLastAddedMarker != null){
            mLastAddedMarker!!.remove()
        }
        mCurrentLocMarkerOption = MarkerOptions().position(newLoc).title(context.getString(R.string.my_current_location))
        mLastAddedMarker = mGoogleMap!!.addMarker(mCurrentLocMarkerOption)
    }
}
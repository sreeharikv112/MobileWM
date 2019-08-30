package com.wmapp.ui.utility

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import com.wmapp.R
import java.util.ArrayList

class AssetRenderer(var context:Context, var googleMap:GoogleMap,
                    var clusterManager : ClusterManager<POIClusterItem>)
    : DefaultClusterRenderer<POIClusterItem>(context,googleMap,clusterManager)
{

    //----------------------  Class Variables -------------------------------//
    private var mIconGenerator: IconGenerator
    private var mClusterIconGenerator: IconGenerator
    private var mImageView: ImageView
    private var mClusterImageView: ImageView
    private var mDimension: Int
    private var mContext: Context

    //----------------------  Constructor's  -----------------------------------//
    init{
        mContext = context
        mIconGenerator = IconGenerator(mContext)
        mClusterIconGenerator = IconGenerator(mContext)

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val multiProfile: View
        multiProfile = inflater.inflate(R.layout.multi_profile, null)
        mClusterIconGenerator.setContentView(multiProfile)
        mClusterImageView = multiProfile.findViewById(R.id.image)

        mImageView = ImageView(mContext)
        mDimension = mContext.resources.getDimension(R.dimen.custom_profile_image).toInt()
        mImageView.layoutParams = ViewGroup.LayoutParams(mDimension, mDimension)
        val padding = mContext.resources.getDimension(R.dimen.margin_5).toInt()
        mImageView.setPadding(padding, padding, padding, padding)
        mIconGenerator.setContentView(mImageView)
    }

    //--------------------------------  Methods  --------------------------------//

    /**
     * Draw a single Item.
     * Set the info window to show the title.
     * @param item Individual item
     * @param markerOptions Marker option object
     */
    override fun onBeforeClusterItemRendered(item: POIClusterItem, markerOptions: MarkerOptions) {
        mImageView.setImageResource(item.getProfilePhoto())
        val icon = mIconGenerator.makeIcon()
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(item.title)
    }

    /**
     * Draw multiple Items.
     * @param cluster Cluster object
     * @param markerOptions Marker option object
     */
    override fun onBeforeClusterRendered(
        cluster: Cluster<POIClusterItem>,
        markerOptions: MarkerOptions
    ) {

        val profilePhotos = ArrayList<Drawable>(Math.min(4, cluster.size))
        val width = mDimension
        val height = mDimension

        for (p in cluster.items) {
            // Draw 4 at most.
            val mCount = 4
            if (profilePhotos.size == mCount) break
            val drawable = mContext.resources.getDrawable(p.getProfilePhoto())
            drawable.setBounds(0, 0, width, height)
            profilePhotos.add(drawable)
        }
        val multiDrawable = MultiDrawable(profilePhotos)
        multiDrawable.setBounds(0, 0, width, height)

        mClusterImageView.setImageDrawable(multiDrawable)
        val icon = mClusterIconGenerator.makeIcon(cluster.size.toString())
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
    }

    override fun shouldRenderAsCluster(cluster: Cluster<POIClusterItem>): Boolean {

        return cluster.size > 1
    }
}
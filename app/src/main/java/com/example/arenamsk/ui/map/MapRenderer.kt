package com.example.arenamsk.ui.map

import android.content.Context
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.example.arenamsk.R
import com.example.arenamsk.models.PlaceItem
import com.example.arenamsk.utils.getSportIcon
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import com.google.maps.android.ui.SquareTextView

class MapRenderer(
    val context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<PlaceItem>
) : DefaultClusterRenderer<PlaceItem>(context, map, clusterManager) {

    //Вызывается при отрисовке маркера
    override fun onBeforeClusterItemRendered(item: PlaceItem?, markerOptions: MarkerOptions?) {
        markerOptions?.icon(
            if (item!!.place.playgroundModels.size > 1) {
                "sk".getSportIcon()
            } else {
                item.place.playgroundModels[0].sport?.name.getSportIcon()
            }
        )

        super.setMinClusterSize(1)
        super.onBeforeClusterItemRendered(item, markerOptions)
    }
}
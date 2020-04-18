package com.example.arenamsk.ui.map

import android.content.Context
import com.example.arenamsk.models.PlaceItem
import com.example.arenamsk.utils.getSportIcon
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class MapRenderer(
    val context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<PlaceItem>
) : DefaultClusterRenderer<PlaceItem>(context, map, clusterManager) {

    //Вызывается при отрисовке маркера
    override fun onBeforeClusterItemRendered(item: PlaceItem?, markerOptions: MarkerOptions?) {
        markerOptions?.icon(
            if (item!!.place.playgroundModels.size > 1) {
                "sk".getSportIcon(context)
            } else {
                item.place.playgroundModels[0].sport?.name.getSportIcon(context)
            }
        )

        super.setMinClusterSize(1)
        super.onBeforeClusterItemRendered(item, markerOptions)
    }
}
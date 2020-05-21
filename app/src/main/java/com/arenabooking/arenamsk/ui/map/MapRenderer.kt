package com.arenabooking.arenamsk.ui.map

import android.content.Context
import com.arenabooking.arenamsk.models.PlaceItem
import com.arenabooking.arenamsk.utils.getSportIcon
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

/** Класс отвечает за то как выглядят кластеры и точки */
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
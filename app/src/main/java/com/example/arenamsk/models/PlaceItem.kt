package com.example.arenamsk.models

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class PlaceItem(val place: PlaceModel) : ClusterItem {
    override fun getPosition(): LatLng {
        return LatLng(place.latitude, place.longitude)
    }

    override fun getSnippet() = ""

    override fun getTitle() = place.placeTitle
}
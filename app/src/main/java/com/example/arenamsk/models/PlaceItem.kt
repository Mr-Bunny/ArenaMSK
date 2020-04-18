package com.example.arenamsk.models

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class PlaceItem(var place: PlaceModel) : ClusterItem {

    override fun getSnippet() = ""

    override fun getTitle() = place.placeTitle

    override fun getPosition() = LatLng(place.latitude, place.longitude)
}
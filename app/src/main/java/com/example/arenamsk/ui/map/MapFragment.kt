package com.example.arenamsk.ui.map

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.example.arenamsk.R
import com.example.arenamsk.ui.base.BaseFragment
import com.example.arenamsk.ui.places.PlacesViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class MapFragment : BaseFragment(R.layout.fragment_map), OnMapReadyCallback {

    private var mMap: GoogleMap? = null

    private val mapViewModel by lazy {
        ViewModelProviders.of(this).get(MapViewModel::class.java)
    }

    //Нам также нужна PlacesViewModel для получения списка площадок
    private val placeViewModel by lazy {
        ViewModelProviders.of(this).get(PlacesViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
    }
}
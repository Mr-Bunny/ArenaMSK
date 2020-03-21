package com.example.arenamsk.ui.map

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.example.arenamsk.R
import com.example.arenamsk.ui.base.BaseFragment
import com.example.arenamsk.ui.places.PlacesViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : BaseFragment(R.layout.fragment_map), OnMapReadyCallback {

    private var mMap: GoogleMap? = null

    private val args: MapFragmentArgs by navArgs()

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

        args.coordinates?.let {
            mMap?.addMarker(MarkerOptions().position(LatLng(it.latitude, it.longitude)))

            val width = DisplayMetrics().also {
                requireActivity().windowManager.defaultDisplay.getMetrics(it)
            }.widthPixels

            val latLngBuilder = LatLngBounds.Builder().apply {
                include(LatLng(it.latitude, it.longitude))
            }
            val latLngBounds: LatLngBounds = latLngBuilder.build()
            val track = CameraUpdateFactory.newLatLngBounds(
                latLngBounds,
                width,
                width,
                25
            )

            mMap?.moveCamera(track)
        }
    }
}
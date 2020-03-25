package com.example.arenamsk.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.example.arenamsk.R
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.ui.base.BaseFragment
import com.example.arenamsk.ui.place_filter.PlaceFilterFragment
import com.example.arenamsk.ui.places.PlacesViewModel
import com.example.arenamsk.utils.MyLocation
import com.example.arenamsk.utils.disable
import com.example.arenamsk.utils.enable
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_map.*

class MapFragment : BaseFragment(R.layout.fragment_map), OnMapReadyCallback {

    companion object {
        private const val ZOOM_VALUE = 2f
        private const val LOCATION_REFRESH_TIME: Long = 10
        private const val LOCATION_REFRESH_DISTANCE: Float = 10f
    }

    private var currentLocation: Location? = null

    private val mLocationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            currentLocation = location
        }

        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
        }

        override fun onProviderEnabled(p0: String?) {
        }

        override fun onProviderDisabled(p0: String?) {
        }
    }

    private var mLocationManager: LocationManager? = null

    private var currentZoom = 1f
    set(value) {
        field = if (value < 1f) 1f else value
    }

    private var mMap: GoogleMap? = null

    private val args: MapFragmentArgs by navArgs()

    private val mapViewModel by lazy {
        ViewModelProviders.of(this).get(MapViewModel::class.java)
    }

    //Нам также нужна PlacesViewModel для получения списка площадок
    private val placeViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(PlacesViewModel::class.java)
    }

    private var placeFilterFragment: PlaceFilterFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        placeViewModel.updatePlaceWithFilter()

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        map_filter_button.setOnClickListener {
            openFilterFragment()
        }

        map_search_button.setOnClickListener {
            setSearchState(false)
        }

        map_edit_text_search.setOnFocusChangeListener { _, isFocus ->
            if (!isFocus && map_edit_text_search.text.toString().trim().isEmpty()) {
                setSearchState(true)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        mMap?.moveCamera( CameraUpdateFactory.newLatLngZoom(LatLng(55.753215, 37.622504), 12f))

        map_plus_button.setOnClickListener {
            currentZoom += ZOOM_VALUE
            mMap?.animateCamera(CameraUpdateFactory.zoomIn())
        }

        map_minus_button.setOnClickListener {
            currentZoom -= ZOOM_VALUE
            mMap?.animateCamera(CameraUpdateFactory.zoomOut())
        }

        map_geo_button.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            } else {
                mMap?.let { it.isMyLocationEnabled = true }

                val locationResult = object : MyLocation.LocationResult() {

                    override fun gotLocation(location: Location?) {
                        val lat = location!!.latitude
                        val lon = location.longitude

                        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lon), 15f))
                    }
                }

                val myLocation = MyLocation()
                myLocation.getLocation(requireContext(), locationResult)
            }
        }

        placeViewModel.getPlacesLiveData().observe(viewLifecycleOwner, Observer {
            mMap?.clear()
            showPlacesOnMap(it)
        })

        //По идее это для отображения точки при переходе с экрана площадок
//        args.coordinates?.let {
//            mMap?.addMarker(MarkerOptions().position(LatLng(it.latitude, it.longitude)))
//
//            val width = DisplayMetrics().also {
//                requireActivity().windowManager.defaultDisplay.getMetrics(it)
//            }.widthPixels
//
//            val latLngBuilder = LatLngBounds.Builder().apply {
//                include(LatLng(it.latitude, it.longitude))
//            }
//            val latLngBounds: LatLngBounds = latLngBuilder.build()
//            val track = CameraUpdateFactory.newLatLngBounds(
//                latLngBounds,
//                width,
//                width,
//                25
//            )
//
//            mMap?.moveCamera(track)
//        }
    }

    private fun showPlacesOnMap(list: List<PlaceModel>) {
        val placesCoordinates = mutableListOf<LatLng>()
        list.forEach { placesCoordinates.add(LatLng(it.latitude, it.longitude)) }

        val markers = arrayListOf<MarkerOptions>()
        placesCoordinates.forEach {
            markers.add(MarkerOptions().position(it).apply { mMap?.addMarker(this) })
        }
    }

    /** @param isEmptyText if true we set fab is visible and hide edit text */
    private fun setSearchState(isEmptyText: Boolean) {
        if (isEmptyText) {
            map_search_button.enable()
            map_edit_text_search.disable()
        } else {
            map_search_button.disable()
            map_edit_text_search.enable()
        }
    }

    private fun openFilterFragment() {
        placeFilterFragment?.dismiss()
        placeFilterFragment = PlaceFilterFragment.getInstance()
        placeFilterFragment?.show(
            activity!!.supportFragmentManager,
            PlaceFilterFragment.FILTER_MODEL_TAG
        )
    }
}
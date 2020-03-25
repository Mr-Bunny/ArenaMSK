package com.example.arenamsk.ui.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.FrameLayout
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.example.arenamsk.App
import com.example.arenamsk.R
import com.example.arenamsk.models.PlaceItem
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.ui.base.BaseFragment
import com.example.arenamsk.ui.place_filter.PlaceFilterFragment
import com.example.arenamsk.ui.places.PlacesViewModel
import com.example.arenamsk.utils.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.maps.android.clustering.ClusterManager
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.map_bottom_layout.*

class MapFragment : BaseFragment(R.layout.fragment_map), OnMapReadyCallback {

    companion object {
        private const val ZOOM_VALUE = 2f
        private const val GEO_REQUEST = 1002
        private const val LOCATION_REFRESH_TIME: Long = 10
        private const val LOCATION_REFRESH_DISTANCE: Float = 10f
    }

    private var currentLocation: Location? = null

    private var clusterManager: ClusterManager<PlaceItem>? = null

    private lateinit var bottomDialogBehavior: BottomSheetBehavior<FrameLayout>

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkPermission()

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

        bottomDialogBehavior = BottomSheetBehavior.from(marker_info)
        bottomDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        mMap?.moveCamera( CameraUpdateFactory.newLatLngZoom(LatLng(55.753215, 37.622504), 12f))

        setClusterParameters()

        mMap?.setOnMapClickListener {
            map_edit_text_search.clearFocus()
            bottomDialogBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)
        }

        map_plus_button.setOnClickListener {
            currentZoom += ZOOM_VALUE
            mMap?.animateCamera(CameraUpdateFactory.zoomIn())
        }

        map_minus_button.setOnClickListener {
            currentZoom -= ZOOM_VALUE
            mMap?.animateCamera(CameraUpdateFactory.zoomOut())
        }

        map_geo_button.setOnClickListener {
            if (checkPermission()) {
                updateUserLocation()

                mMap?.let { it.isMyLocationEnabled = true }

                MyLocation.userLocation?.let {
                    mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 15f))
                }
            }
        }

        placeViewModel.getPlacesLiveData().observe(viewLifecycleOwner, Observer {
            showPlacesOnMap(it)
        })

        placeViewModel.getFoundedPlacesLiveData().observe(viewLifecycleOwner, Observer {
            showPlacesOnMap(it)
        })

        //Вешаем слушатель на поле поиска площадок по названию и адресу
        map_edit_text_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(textToSearch: Editable?) {
                placeViewModel.showFilteredPlaces(textToSearch.toString().trim())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(textToSearch: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
       if (requestCode == GEO_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            updateUserLocation()
       }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun showPlacesOnMap(list: List<PlaceModel>) {
        mMap?.clear()
        clusterManager?.clearItems()

        list.forEach {
            val item = PlaceItem(it)
            clusterManager?.addItem(item)
        }

        clusterManager?.cluster()

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
            with(map_edit_text_search) {
                disable()
                clearFocus()
                hideKeyboard()
            }
        } else {
            map_search_button.disable()
            with(map_edit_text_search) {
                enable()
                requestFocus()
                showKeyboard()
            }
        }
    }

    private fun setClusterParameters() {
        clusterManager = ClusterManager(requireContext(), mMap)
        clusterManager?.setOnClusterItemClickListener { item -> onClusterItemClick(item.place) }

        mMap?.let {
            it.setOnMapClickListener {
                map_edit_text_search.clearFocus()
                bottomDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }

    private fun onClusterItemClick(place: PlaceModel): Boolean {
        bottomDialogBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        setPlaceInfo(place)

        return true
    }

    private fun setPlaceInfo(place: PlaceModel) {
        map_place_title.text = place.placeTitle
        map_item_work_time_text.text = TimeUtils.convertWorkTime(place.workDayStartAt, place.workDayEndAt)
        map_item_address_text.text = place.address
        map_place_distance.text = resources.getString(R.string.text_place_distance, MyLocation.calculateDistance(
            LatLng(place.latitude, place.longitude)
        ).toString())
    }

    private fun openFilterFragment() {
        placeFilterFragment?.dismiss()
        placeFilterFragment = PlaceFilterFragment.getInstance()
        placeFilterFragment?.show(
            activity!!.supportFragmentManager,
            PlaceFilterFragment.FILTER_MODEL_TAG
        )
    }

    /** return true if permission granted */
    private fun checkPermission(): Boolean {
        return if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), GEO_REQUEST)
            false
        } else {
            true
        }
    }
}
package com.example.arenamsk.ui.map

import android.Manifest
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
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm
import com.google.maps.android.collections.MarkerManager
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.map_bottom_layout.*

class MapFragment : BaseFragment(R.layout.fragment_map), OnMapReadyCallback {

    companion object {
        private const val ZOOM_VALUE = 2f
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

    private fun showPlacesOnMap(list: List<PlaceModel>) {
        mMap?.clear()

        list.forEach {
            val item = PlaceItem(it)
            clusterManager?.addItem(item)
        }

        val placesCoordinates = mutableListOf<LatLng>()
        list.forEach { placesCoordinates.add(LatLng(it.latitude, it.longitude)) }

        val markers = arrayListOf<MarkerOptions>()
        placesCoordinates.forEach {
            markers.add(MarkerOptions().position(it).apply { mMap?.addMarker(this) })
        }

        clusterManager?.cluster()
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

    private fun setClusterParameters() {
        clusterManager = ClusterManager(requireContext(), mMap)
        clusterManager?.setOnClusterItemClickListener { item -> onClusterItemClick(item.place) }

        mMap?.let {
            it.setOnCameraIdleListener(clusterManager)
            it.setOnMarkerClickListener(clusterManager)
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
        //TODO
        //map_place_distance.text
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
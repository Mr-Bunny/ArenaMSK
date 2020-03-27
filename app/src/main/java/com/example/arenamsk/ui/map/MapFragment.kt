package com.example.arenamsk.ui.map

import android.Manifest
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.example.arenamsk.App
import com.example.arenamsk.R
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.ui.base.BaseFragment
import com.example.arenamsk.ui.base.PlaceDialogFragment
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
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.map_bottom_layout.*
import kotlinx.android.synthetic.main.map_marker_info_window.view.*

class MapFragment : BaseFragment(R.layout.fragment_map), OnMapReadyCallback {

    companion object {
        private const val ZOOM_VALUE = 2f
        private const val GEO_REQUEST = 1002
    }

    private var currentLocation: Location? = null

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

    private var placeDetailFragment: PlaceDialogFragment? = null
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

        //Если есть аргументы, то значит был переход с другого экрана и нам надо показать нужную площадку
        val coordinatesModel = args.coordinates
        if (coordinatesModel != null) {
            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(coordinatesModel.latitude, coordinatesModel.longitude), 17f))
        } else {
            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(55.753215, 37.622504), 12f))
        }

        mMap?.setOnMapClickListener {
            with(map_edit_text_search) {
                clearFocus()
                hideKeyboard()
            }

            bottomDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

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

    /** Добавляем площадки на карту */
    private fun showPlacesOnMap(list: List<PlaceModel>) {
        mMap?.clear()
        mMap?.setOnMarkerClickListener {
            onClusterItemClick(it)
            true
        }

        marker_info.setOnClickListener {
            placeViewModel.getPlaceByTilte(map_place_title.text.toString())?.let {
                placeDetailFragment?.dismiss()
                placeDetailFragment = PlaceDialogFragment.getInstance(it, false)
                placeDetailFragment?.show(
                    activity!!.supportFragmentManager,
                    PlaceDialogFragment.PLACE_DIALOG_FRAGMENT_TAG
                )
            }
        }

        list.forEach { place ->
            val coordinates = LatLng(place.latitude, place.longitude)
            MarkerOptions().position(coordinates).apply {
                mMap?.addMarker(this)?.let {
                    //set tag
                    it.tag = place.id
                    //set icon
                    if (place.playgroundModels.size > 1) {
                        it.setIcon("sk".getSportIcon())
                    } else if (place.playgroundModels.isNotEmpty()) {
                        it.setIcon(place.playgroundModels[0].sport?.name.getSportIcon())
                    }
                    //set coordinates
                    val coordinatesModel = args.coordinates
                    if (coordinatesModel != null &&
                        (coordinatesModel.latitude == place.latitude &&
                                coordinatesModel.longitude == place.longitude)
                    ) onClusterItemClick(it)

                    //it.showInfoWindow()
                }
            }
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

    private fun onClusterItemClick(placeMarker: Marker): Boolean {
        placeViewModel.getPlaceByMarker(placeMarker)?.let {
            mMap?.moveCamera(CameraUpdateFactory.newLatLng(LatLng(it.latitude, it.longitude)))
            bottomDialogBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            setPlaceInfo(it)
        }

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
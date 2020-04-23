package com.example.arenamsk.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.example.arenamsk.models.PlaceModel
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import java.util.*

/** Класс для получения информации о местоположении пользователя */
class MyLocation {
    internal lateinit var timer1: Timer
    internal var lm: LocationManager? = null
    internal lateinit var locationResult: LocationResult
    internal var gps_enabled = false
    internal var network_enabled = false

    companion object {
        var userLocation: LatLng? = null

        fun calculateDistance(placeLocation: LatLng): Float {
            return if (userLocation == null) {
                0f
            } else if (placeLocation.latitude != 0.0 && placeLocation.longitude != 0.0) {
                SphericalUtil.computeDistanceBetween(LatLng(placeLocation.latitude, placeLocation.longitude), userLocation).toFloat()
            } else {
                0f
            }
        }

        fun getPlacesWithDistance(places: List<PlaceModel>): List<PlaceModel> {
            if (userLocation == null) return places

            val coordinates = LatLng(userLocation!!.latitude, userLocation!!.longitude)

            places.forEach {
                if (it.latitude != 0.0 && it.longitude != 0.0) {
                    val distance = SphericalUtil.computeDistanceBetween(LatLng(it.latitude, it.longitude), coordinates)
                    it.distance = distance.toFloat()
                } else {
                    it.distance = 0f
                }
            }

            return places
        }
    }

    internal var locationListenerGps: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            timer1.cancel()
            locationResult.gotLocation(location)
            lm!!.removeUpdates(this)
            lm!!.removeUpdates(locationListenerNetwork)
        }

        override fun onProviderDisabled(provider: String) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    }

    internal var locationListenerNetwork: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            timer1.cancel()
            locationResult.gotLocation(location)
            lm!!.removeUpdates(this)
            lm!!.removeUpdates(locationListenerGps)
        }

        override fun onProviderDisabled(provider: String) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    }

    fun getLocation(context: Context, result: LocationResult): Boolean {
        //I use LocationResult callback class to pass location value from MyLocation to user code.
        locationResult = result
        if (lm == null)
            lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?

        //exceptions will be thrown if provider is not permitted.
        try {
            gps_enabled = lm!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
        }

        try {
            network_enabled = lm!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
        }

        //don't start listeners if no provider is enabled
        if (!gps_enabled && !network_enabled)
            return false

        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) run {

            ActivityCompat.requestPermissions(context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 111)
        }


        if (gps_enabled)
            lm!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListenerGps)
        if (network_enabled)
            lm!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListenerNetwork)
        timer1 = Timer()
        timer1.schedule(GetLastLocation(context), 20000)
        return true
    }

    internal inner class GetLastLocation(var context: Context) : TimerTask() {
        override fun run() {
            lm!!.removeUpdates(locationListenerGps)
            lm!!.removeUpdates(locationListenerNetwork)

            var net_loc: Location? = null
            var gps_loc: Location? = null

            if (ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) run {

                ActivityCompat.requestPermissions(context as Activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),111)
            }


            if (gps_enabled)
                gps_loc = lm!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (network_enabled)
                net_loc = lm!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

            //if there are both values use the latest one
            if (gps_loc != null && net_loc != null) {
                if (gps_loc.getTime() > net_loc.getTime())
                    locationResult.gotLocation(gps_loc)
                else
                    locationResult.gotLocation(net_loc)
                return
            }

            if (gps_loc != null) {
                locationResult.gotLocation(gps_loc)
                return
            }
            if (net_loc != null) {
                locationResult.gotLocation(net_loc)
                return
            }
            locationResult.gotLocation(null)
        }
    }

    abstract class LocationResult {
        abstract fun gotLocation(location: Location?)
    }
}
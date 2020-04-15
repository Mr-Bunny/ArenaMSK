package com.example.arenamsk.ui.base

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.example.arenamsk.App
import com.example.arenamsk.MobileNavigationDirections
import com.example.arenamsk.R
import com.example.arenamsk.models.CoordinatesModel
import com.example.arenamsk.network.utils.AuthUtils
import com.example.arenamsk.ui.AuthActivity
import com.example.arenamsk.ui.MainActivity
import com.example.arenamsk.utils.MyLocation
import com.example.arenamsk.utils.getStatusBarHeight
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_booked.*

abstract class BaseFragment(private val layoutId: Int): Fragment(), LifecycleOwner {

    private var toast: Toast? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Получаем позицию пользователя
        updateUserLocation()
        return inflater.inflate(layoutId, container, false)
    }

    override fun onDestroyView() {
        toast?.cancel()

        super.onDestroyView()
    }

    protected fun showToast(msg: String) {
        toast?.cancel()
        toast = Toast.makeText(context, msg, Toast.LENGTH_LONG).apply { show() }
    }

    protected fun showToast(msgId: Int) {
        toast?.cancel()
        toast = Toast.makeText(context, msgId, Toast.LENGTH_LONG).apply { show() }
    }

    protected fun showNetworkOfflineError() {
        showToast(R.string.network_offline_text)
    }

    protected fun openPhone(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phone")
        }
        startActivity(intent)
    }

    protected fun openMap(coordinatesModel: CoordinatesModel) {
        MobileNavigationDirections.actionOpenPlaceOnMap(coordinatesModel).also {
            findNavController().navigate(it)
        }
    }

    protected fun exitFromProfile() {
        with(AuthUtils) {
            setUserIsAuthorized(false)
            setUserIsDefault(false)
            saveAuthToken("")
            saveRefreshToken("")
        }

        startActivity(Intent(activity, AuthActivity::class.java))
        activity?.finish()
    }

    /** Получаем позицию пользователя */
    protected fun updateUserLocation() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val locationResult = object : MyLocation.LocationResult() {
                override fun gotLocation(location: Location?) {
                    location?.let {
                        MyLocation.userLocation = LatLng(location.latitude, location.longitude)
                    }
                }
            }

            MyLocation().getLocation(requireActivity(), locationResult)
        }
    }

    /** Метод добавляет отступ сверху равный размеру statusBar
     * @param view - View для которого нужно установить размер*/
    protected fun setupTopPadding(view: View) {
        val statusBarHeight = requireContext().getStatusBarHeight()
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, statusBarHeight, 0, 0)
        view.layoutParams = params
    }
}
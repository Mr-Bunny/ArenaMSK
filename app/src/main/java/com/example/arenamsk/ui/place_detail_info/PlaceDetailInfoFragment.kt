package com.example.arenamsk.ui.place_detail_info

import android.os.Bundle
import android.view.View
import com.example.arenamsk.R
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.ui.base.BaseFragment
import com.example.arenamsk.ui.booking.PlaceBookingFragment
import com.example.arenamsk.ui.place_detail.PlaceDetailFragment
import com.example.arenamsk.ui.place_detail.PlaceDetailFragment.Companion.PLACE_DETAIL_ARG_TAG
import kotlinx.android.synthetic.main.fragment_place_info.*

class PlaceDetailInfoFragment private constructor() : BaseFragment(R.layout.fragment_place_info) {

    companion object {
        fun getInstance(place: PlaceModel? = null): PlaceDetailInfoFragment {
            place?.let {
                return PlaceDetailInfoFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(PLACE_DETAIL_ARG_TAG, it)
                    }
                }
            }


            return PlaceDetailInfoFragment()
        }
    }

    private var placeBookingFragment: PlaceBookingFragment? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        additional_info_container.columnCount = if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) 1 else 2

        //Открываем экран бронирования
        place_detail_info_btn_book.setOnClickListener { openBookingScreen(arguments?.getParcelable(PLACE_DETAIL_ARG_TAG)) }
    }

    /** Открываем экран просмотра расписания и бронирования */
    private fun openBookingScreen(place: PlaceModel?) {
        placeBookingFragment?.dismiss()
        placeBookingFragment = PlaceBookingFragment.getInstance(place)
        placeBookingFragment?.show(activity!!.supportFragmentManager, PlaceBookingFragment.PLACE_BOOKING_TAG)
    }
}

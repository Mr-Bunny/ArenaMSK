package com.example.arenamsk.ui.place_detail_info

import android.os.Bundle
import android.view.View
import com.example.arenamsk.R
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.ui.base.BaseFragment
import com.example.arenamsk.ui.place_detail.PlaceDetailFragment.Companion.PLACE_DETAIL_ARG_TAG

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}

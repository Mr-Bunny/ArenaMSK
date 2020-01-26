package com.example.arenamsk.ui.place_detail_photo

import android.os.Bundle
import android.view.View
import com.example.arenamsk.R
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.ui.base.BaseFragment
import com.example.arenamsk.ui.place_detail.PlaceDetailFragment.Companion.PLACE_DETAIL_ARG_TAG

class PlaceDetailPhotoFragment : BaseFragment(R.layout.fragment_place_detail_photo) {

    companion object {
        fun getInstance(place: PlaceModel? = null): PlaceDetailPhotoFragment {
            place?.let {
                return PlaceDetailPhotoFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(PLACE_DETAIL_ARG_TAG, it)
                    }
                }
            }

            return PlaceDetailPhotoFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}

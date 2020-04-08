package com.example.arenamsk.ui.booked.booked_pager_item

import android.os.Bundle
import android.view.View
import com.example.arenamsk.R
import com.example.arenamsk.ui.base.BaseFragment
import com.example.arenamsk.ui.place_detail.PlaceDetailFragment.Companion.PLACE_DETAIL_ARG_TAG
import com.example.arenamsk.ui.place_detail_info.PlaceDetailInfoFragment

class BookedPagerItemFragment: BaseFragment(R.layout.fragment_booked_pager_item) {

    companion object {
        fun getInstance(): BookedPagerItemFragment {
//            return BookedPagerItemFragment().apply {
//                arguments = Bundle().apply {
//                    putParcelable(PLACE_DETAIL_ARG_TAG, it)
//                }
//            }
            return BookedPagerItemFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
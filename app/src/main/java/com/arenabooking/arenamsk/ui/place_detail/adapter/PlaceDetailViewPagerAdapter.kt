package com.arenabooking.arenamsk.ui.place_detail.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.arenabooking.arenamsk.App
import com.arenabooking.arenamsk.R
import com.arenabooking.arenamsk.models.PlaceModel
import com.arenabooking.arenamsk.ui.place_detail_feedback.PlaceDetailFeedbackFragment
import com.arenabooking.arenamsk.ui.place_detail_info.PlaceDetailInfoFragment
import com.arenabooking.arenamsk.ui.place_detail_photo.PlaceDetailPhotoFragment

class PlaceDetailViewPagerAdapter(
    private val placeModel: PlaceModel,
    fm: FragmentManager
) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int = 3

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> {
            PlaceDetailInfoFragment.getInstance(placeModel)
        }

        1 -> {
            PlaceDetailPhotoFragment.getInstance(placeModel)
        }

        else -> {
            PlaceDetailFeedbackFragment.getInstance(placeModel)
        }
    }

    override fun getPageTitle(position: Int): CharSequence? =
        App.appContext().resources.getStringArray(R.array.place_detail_view_pager_titles)[position]

}
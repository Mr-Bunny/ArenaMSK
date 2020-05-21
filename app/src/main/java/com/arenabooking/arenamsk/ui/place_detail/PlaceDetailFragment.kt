package com.arenabooking.arenamsk.ui.place_detail

import android.os.Bundle
import android.view.*
import com.arenabooking.arenamsk.R
import com.arenabooking.arenamsk.models.PlaceModel
import com.arenabooking.arenamsk.ui.base.BaseFragment
import com.arenabooking.arenamsk.ui.place_detail.adapter.PlaceDetailViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_place_detail.*

/** Экран с информацией о площадке
 * Здесь во viewPager отображаются фрагменты с информацией, фото и отзывами */
class PlaceDetailFragment : BaseFragment(R.layout.fragment_place_detail) {

    companion object {
        const val PLACE_DETAIL_TAG = "place_detail_tag"
        const val PLACE_DETAIL_ARG_TAG = "place_detail_arg_tag"
    }

    private val placeFilterModel by lazy {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateUI()
    }

    private fun updateUI() {
        //Get Place from args if null dismiss fragment
        val place: PlaceModel = arguments?.getParcelable(PLACE_DETAIL_ARG_TAG) ?: PlaceModel()

        with(place_detail_view_pager) {
            adapter = PlaceDetailViewPagerAdapter(place, childFragmentManager)
            place_detail_tab_layout.setupWithViewPager(this)
        }
    }
}
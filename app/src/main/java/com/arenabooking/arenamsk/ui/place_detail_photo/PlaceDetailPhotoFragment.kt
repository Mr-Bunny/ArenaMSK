package com.arenabooking.arenamsk.ui.place_detail_photo

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.arenabooking.arenamsk.R
import com.arenabooking.arenamsk.models.PlaceModel
import com.arenabooking.arenamsk.ui.base.BaseFragment
import com.arenabooking.arenamsk.ui.place_detail.PlaceDetailFragment.Companion.PLACE_DETAIL_ARG_TAG
import com.arenabooking.arenamsk.ui.place_detail_photo.adapter.PlaceDetailPhotoAdapter
import kotlinx.android.synthetic.main.fragment_place_detail_photo.*

/** Фрагмент с офтографиями площадки */
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

    private val photoAdapter by lazy { PlaceDetailPhotoAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()

        //Show Place photos from args
        val place: PlaceModel = arguments?.getParcelable(PLACE_DETAIL_ARG_TAG) ?: PlaceModel()
        photoAdapter.setNewList(place.images)
    }

    private fun initRecycler() {
        with(place_detail_photo_recycler) {
            setHasFixedSize(true)
            adapter = photoAdapter
            layoutManager = GridLayoutManager(
                context,
                3,
                GridLayoutManager.VERTICAL,
                false
            )
        }
    }

}

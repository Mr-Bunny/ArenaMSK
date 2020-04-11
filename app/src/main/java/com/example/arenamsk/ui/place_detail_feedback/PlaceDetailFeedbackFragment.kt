package com.example.arenamsk.ui.place_detail_feedback

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arenamsk.R
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.ui.base.BaseFragment
import com.example.arenamsk.ui.place_detail.PlaceDetailFragment.Companion.PLACE_DETAIL_ARG_TAG
import com.example.arenamsk.ui.place_detail_feedback.adapter.PlaceDetailFeedbackAdapter
import com.example.arenamsk.ui.places.PlacesViewModel
import com.example.arenamsk.utils.disable
import com.example.arenamsk.utils.enable
import kotlinx.android.synthetic.main.fragment_place_detail_feedback.*

class PlaceDetailFeedbackFragment private constructor() :
    BaseFragment(R.layout.fragment_place_detail_feedback) {

    companion object {
        fun getInstance(place: PlaceModel? = null): PlaceDetailFeedbackFragment {
            place?.let {
                return PlaceDetailFeedbackFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(PLACE_DETAIL_ARG_TAG, it)
                    }
                }
            }

            return PlaceDetailFeedbackFragment()
        }
    }

    private val feedbackViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(PlaceDetailFeedbackViewModel::class.java)
    }

    private val feedbackAdapter by lazy { PlaceDetailFeedbackAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()

        val place = arguments?.getParcelable(PLACE_DETAIL_ARG_TAG) ?: PlaceModel()

        feedbackViewModel.getFeedbackList(place.id.toString())

        feedbackViewModel.getFeedbackListLiveData().observe(viewLifecycleOwner, Observer {
            //Show feedback of place
            if (!it.isNullOrEmpty()) feedbackAdapter.setNewList(it)

            //Если бронировали и отзывов нет
            if (place.inHistory && it.isNullOrEmpty()) {
                //TODO set OnClick to the feedback_no_reviews_text
                feedback_no_reviews_text_send_text_view.enable()
            } else if (!place.inHistory && it.isNullOrEmpty()) { //Если не бронировали и отзывов нет
                feedback_no_reviews_text_view.enable()
            } else if (place.inHistory && it.isNotEmpty()) { //Если бронировали и есть отзывы
                feedback_button.enable()
            }
        })
    }

    private fun initRecycler() {
        with(recycler_feedback) {
            adapter = feedbackAdapter
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
        }
    }

}

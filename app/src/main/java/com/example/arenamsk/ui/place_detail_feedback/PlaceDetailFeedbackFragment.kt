package com.example.arenamsk.ui.place_detail_feedback

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arenamsk.R
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.ui.base.BaseFragment
import com.example.arenamsk.ui.place_detail.PlaceDetailFragment.Companion.PLACE_DETAIL_ARG_TAG
import com.example.arenamsk.ui.place_detail_feedback.adapter.PlaceDetailFeedbackAdapter
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

    private val feedbackAdapter by lazy { PlaceDetailFeedbackAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()

        //Show feedback of place
        with(arguments?.getParcelable(PLACE_DETAIL_ARG_TAG) ?: PlaceModel()) {
            feedbackAdapter.setNewList(feedbackList)

            //Если бронировали и отзывов нет
            if (inHistory && feedbackList.isNullOrEmpty()) {
                //TODO set OnClick to the feedback_no_reviews_text
                feedback_no_reviews_text_send_text_view.enable()
            } else if (!inHistory && feedbackList.isNullOrEmpty()) { //Если не бронировали и отзывов нет
                feedback_no_reviews_text_view.enable()
            } else if (inHistory && feedbackList.isNotEmpty()) { //Если бронировали и есть отзывы
                feedback_button.enable()
            }
        }
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

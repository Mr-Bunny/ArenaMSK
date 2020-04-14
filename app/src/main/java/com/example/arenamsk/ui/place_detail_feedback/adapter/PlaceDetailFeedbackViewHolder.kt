package com.example.arenamsk.ui.place_detail_feedback.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.arenamsk.models.FeedbackModel
import com.example.arenamsk.utils.TimeUtils
import kotlinx.android.synthetic.main.place_feedback_item.view.*


class PlaceDetailFeedbackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(feedback: FeedbackModel) {
        with(itemView) {
            feedback_author.text = feedback.authorName
            feedback_rating.rating = feedback.rating
            feedback_text.text = feedback.feedbackText

            feedback_date.text = TimeUtils.convertTimeStampToDate(
                try {
                    feedback.date.toLong() * 1000L
                } catch (e: Exception) {
                    System.currentTimeMillis()
                }
            )

            feedback_recommendation.visibility =
                if (feedback.isRecommendation) View.VISIBLE else View.GONE
        }
    }
}
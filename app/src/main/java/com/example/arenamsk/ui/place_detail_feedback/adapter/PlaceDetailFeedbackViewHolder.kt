package com.example.arenamsk.ui.place_detail_feedback.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.arenamsk.models.FeedbackModel
import kotlinx.android.synthetic.main.place_feedback_item.view.*


class PlaceDetailFeedbackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(feedback: FeedbackModel) {
        with(itemView) {
            feedback_author.text = feedback.authorName
            feedback_date.text = feedback.date
            feedback_rating.rating = feedback.rating
            feedback_text.text = feedback.feedbackText

            feedback_recommendation.visibility = if (feedback.isRecommendation) View.VISIBLE else View.GONE
        }
    }
}
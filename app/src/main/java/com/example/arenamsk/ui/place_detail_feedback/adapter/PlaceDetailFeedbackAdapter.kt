package com.example.arenamsk.ui.place_detail_feedback.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.arenamsk.R
import com.example.arenamsk.models.FeedbackModel

class PlaceDetailFeedbackAdapter :
    RecyclerView.Adapter<PlaceDetailFeedbackViewHolder>() {

    private val feedbackList: MutableList<FeedbackModel> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaceDetailFeedbackViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.place_feedback_item, parent, false)
        return PlaceDetailFeedbackViewHolder(view)
    }

    override fun onBindViewHolder(holderScreen: PlaceDetailFeedbackViewHolder, position: Int) {
        holderScreen.bind(feedbackList[position])
    }

    override fun getItemCount(): Int = feedbackList.size

    fun setNewList(newList: List<FeedbackModel>) {
        if (feedbackList.isNotEmpty()) feedbackList.clear()
        feedbackList.addAll(newList)
        notifyDataSetChanged()
    }

}
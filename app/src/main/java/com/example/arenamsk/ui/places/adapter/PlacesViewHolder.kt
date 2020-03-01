package com.example.arenamsk.ui.places.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.arenamsk.R
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.ui.places.viewpager.PlaceViewPagerAdapter
import com.example.arenamsk.utils.disable
import com.example.arenamsk.utils.enable
import kotlinx.android.synthetic.main.item_place_card.view.*


class PlacesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(place: PlaceModel,
             itemClickCallback: (meetUpId: PlaceModel) -> Unit,
             itemBookingClickCallback: (place : PlaceModel) -> Unit) {
        with(itemView) {
            place_item_title.text = place.title
            place_item_description.text = place.description

            place_item_rating_bar.rating = place.rating

            place_item_feedback_count_text.text = itemView.context.getString(
                R.string.text_place_feedback_number,
                place.feedbackNumber.toString()
            )

            place_item_work_time_text.text = place.workTime

            place_item_address_text.text = place.address

            with(place_item_favourite_btn) {
                if (place.isFavourite) {
                    setImageDrawable(itemView.context.resources.getDrawable(R.drawable.ic_in_favoutires_btn))
                } else {
                    setImageDrawable(itemView.context.resources.getDrawable(R.drawable.ic_add_to_favourites_btn))
                }
            }

            place_item_distance_text.text = itemView.context.getString(
                R.string.text_place_distance,
                place.distance.toString()
            )

            setOnClickListener { itemClickCallback.invoke(place) }

            place_item_btn_calendar.setOnClickListener { itemBookingClickCallback.invoke(place) }

            when {
                place.imagesUrl.isEmpty() -> {
                    place_item_view_pager.disable()
                    place_item_view_pager_indicator.disable()
                    return
                }
                place.imagesUrl.size == 1 -> {
                    place_item_view_pager.enable()
                    place_item_view_pager_indicator.disable()
                }
                else -> {
                    place_item_view_pager.enable()
                    place_item_view_pager_indicator.enable()
                }
            }

            val adapter = PlaceViewPagerAdapter()
            place_item_view_pager.adapter = adapter
            adapter.setNewImages(place.imagesUrl as MutableList<String>)
            place_item_view_pager_indicator.setViewPager(place_item_view_pager)
        }
    }
}
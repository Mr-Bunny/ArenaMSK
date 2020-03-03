package com.example.arenamsk.ui.booking.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.arenamsk.R
import com.example.arenamsk.models.PlaceBookingModel
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.ui.places.viewpager.PlaceViewPagerAdapter
import com.example.arenamsk.utils.disable
import com.example.arenamsk.utils.enable
import kotlinx.android.synthetic.main.item_place_booking.view.*
import kotlinx.android.synthetic.main.item_place_card.view.*

class PlaceBookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var isSelected = false

    fun bind(place: PlaceBookingModel, itemClickCallback: () -> Unit) {
        with(itemView) {
            setupItems(place, this)

            if (place.statusIsFree) {
                setOnClickListener {
                    isSelected = !isSelected

                    if (isSelected) {
                        this.setBackgroundColor(context.resources.getColor(R.color.colorPrimary))
                        booking_date.setTextColor(context.resources.getColor(R.color.colorWhite))
                        booking_price.setTextColor(context.resources.getColor(R.color.colorWhite))
                        booking_status.setTextColor(context.resources.getColor(R.color.colorWhite))
                    } else {
                        setupItems(place, this)
                    }
                }
            }
        }
    }

    private fun setupItems(place: PlaceBookingModel, itemView: View) {
        with(itemView) {
            this.setBackgroundColor(context.resources.getColor(R.color.colorWhite))

            if (place.statusIsFree) {
                booking_date.setTextColor(context.resources.getColor(R.color.colorPrimary))
                booking_price.setTextColor(context.resources.getColor(R.color.colorPrimary))
                booking_status.setTextColor(context.resources.getColor(R.color.colorPrimary))
                booking_status.text = context.resources.getString(R.string.booking_status_free)
                isClickable = true
            } else {
                booking_date.setTextColor(context.resources.getColor(R.color.place_is_not_free))
                booking_price.setTextColor(context.resources.getColor(R.color.place_is_not_free))
                booking_status.setTextColor(context.resources.getColor(R.color.place_is_not_free))
                booking_status.text = context.resources.getString(R.string.booking_status_not_free)
                isClickable = false
            }
        }
    }
}
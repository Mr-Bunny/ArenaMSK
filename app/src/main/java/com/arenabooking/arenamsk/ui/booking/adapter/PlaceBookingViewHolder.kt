package com.arenabooking.arenamsk.ui.booking.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.arenabooking.arenamsk.R
import com.arenabooking.arenamsk.network.models.BookingDateModel
import com.arenabooking.arenamsk.utils.TimeUtils
import kotlinx.android.synthetic.main.item_place_booking.view.*

class PlaceBookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(bookingTime: BookingDateModel, itemClickCallback: (id: String, isSelected: Boolean) -> Unit) {
        with(itemView) {
            setupItems(bookingTime, this)

            //Если площадка свободна
            if (!bookingTime.isBooked) {
                setOnClickListener {
                    isSelected = !isSelected

                    itemClickCallback.invoke(bookingTime.id.toString(), isSelected)

                    if (isSelected) {
                        this.setBackgroundColor(context.resources.getColor(R.color.colorPrimary))
                        booking_date.setTextColor(context.resources.getColor(R.color.colorWhite))
                        booking_price.setTextColor(context.resources.getColor(R.color.colorWhite))
                        booking_status.setTextColor(context.resources.getColor(R.color.colorWhite))
                    } else {
                        setupItems(bookingTime, this)
                    }
                }
            }
        }
    }

    private fun setupItems(booking: BookingDateModel, itemView: View) {
        with(itemView) {
            this.setBackgroundColor(context.resources.getColor(R.color.colorWhite))

            booking_date.text = TimeUtils.convertWorkTime(booking.from, booking.to)
            booking_price.text = booking.price.toString()

            if (booking.isBooked) {
                booking_date.setTextColor(context.resources.getColor(R.color.place_is_not_free))
                booking_price.setTextColor(context.resources.getColor(R.color.place_is_not_free))
                booking_status.setTextColor(context.resources.getColor(R.color.place_is_not_free))
                booking_status.text = context.resources.getString(R.string.booking_status_not_free)
                isClickable = false
            } else {
                booking_date.setTextColor(context.resources.getColor(R.color.colorPrimary))
                booking_price.setTextColor(context.resources.getColor(R.color.colorPrimary))
                booking_status.setTextColor(context.resources.getColor(R.color.colorPrimary))
                booking_status.text = context.resources.getString(R.string.booking_status_free)
                isClickable = true
            }
        }
    }
}
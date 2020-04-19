package com.example.arenamsk.ui.booked.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.arenamsk.models.OrderModel
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.utils.TimeUtils
import kotlinx.android.synthetic.main.item_current_order.view.*
import java.lang.Exception

class OrdersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(order: OrderModel, itemClickCallback: (place: PlaceModel, position: Int) -> Unit) {
        with(itemView) {
            itemView.setOnClickListener { itemClickCallback.invoke(order.place ?: PlaceModel(), -1) }

            order_playground_text.text = order.playground?.sport?.name ?: "Вид спорта"
            order_price_text.text = order.amount
            order_date_text.text = TimeUtils.convertBookedDateAndTime(order.date, order.booking[0].from, order.booking[0].to)

            val isHalfBooked = try {
                order.booking[0].isHalfBooking
            } catch (e: Exception) {
                false
            }

            order_playground_title.text = if (isHalfBooked) "Половина площадки: " else "Площадка: "

            order.place?.let {
                order_place_work_time_text.text = TimeUtils.convertWorkTime(it.workDayStartAt, it.workDayStartAt)
                order_place_address_text.text = it.address
                order_place_title.text = it.placeTitle
            }
        }
    }
}
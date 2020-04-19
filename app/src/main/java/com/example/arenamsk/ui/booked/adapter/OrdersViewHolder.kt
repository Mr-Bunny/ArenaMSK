package com.example.arenamsk.ui.booked.adapter

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.arenamsk.R
import com.example.arenamsk.models.OrderModel
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.utils.TimeUtils
import kotlinx.android.synthetic.main.current_booked_item.view.*
import kotlinx.android.synthetic.main.item_current_order.view.*
import java.lang.Exception

class OrdersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(order: OrderModel, itemClickCallback: (place: PlaceModel, position: Int) -> Unit) {
        with(itemView) {
            itemView.setOnClickListener { itemClickCallback.invoke(order.place ?: PlaceModel(), -1) }

            order.place?.let {
                order_place_work_time_text.text = TimeUtils.convertWorkTime(it.workDayStartAt, it.workDayStartAt)
                order_place_address_text.text = it.address
                order_place_title.text = it.placeTitle
            }

            //Для каждого значения времени в order.booking создаем view и добавляем в общий контейнер
            order.booking.forEachIndexed { index, bookingModel->
                //Если мы бронировали несколько времен, то будем отображать цифры
                val isNumberVisible = order.booking.size > 1

                //Была ли забронирована половина или целая площадка
                val isHalfBooked = try {
                    bookingModel.isHalfBooking
                } catch (e: Exception) {
                    false
                }

                val bookedTimeView = LayoutInflater.from(itemView.context).inflate(R.layout.current_booked_item, booked_container, false).apply {
                    this.order_playground_text.text = order.playground?.sport?.name ?: "Вид спорта"
                    this.order_price_text.text = bookingModel.price.toString()
                    this.order_date_text.text = TimeUtils.convertBookedDateAndTime(order.date, bookingModel.from, bookingModel.to)

                    this.order_playground_title.text = if (isHalfBooked) "Половина площадки: " else "Площадка: "

                    this.order_number.visibility = if (isNumberVisible) View.VISIBLE else View.GONE
                    val numberText = "${index + 1}) "
                    this.order_number.text = numberText
                }


                booked_container.addView(bookedTimeView)
            }
        }
    }
}
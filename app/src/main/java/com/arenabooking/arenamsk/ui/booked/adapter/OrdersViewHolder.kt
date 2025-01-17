package com.arenabooking.arenamsk.ui.booked.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.arenabooking.arenamsk.R
import com.arenabooking.arenamsk.models.OrderModel
import com.arenabooking.arenamsk.models.PlaceModel
import com.arenabooking.arenamsk.ui.webview.WebActivity
import com.arenabooking.arenamsk.utils.Constants
import com.arenabooking.arenamsk.utils.TimeUtils
import com.arenabooking.arenamsk.utils.disable
import com.arenabooking.arenamsk.utils.enable
import kotlinx.android.synthetic.main.current_booked_item.view.*
import kotlinx.android.synthetic.main.item_current_order.view.*
import kotlin.Exception

class OrdersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(order: OrderModel, itemClickCallback: (place: PlaceModel, position: Int) -> Unit) {
        with(itemView) {
            itemView.setOnClickListener { itemClickCallback.invoke(order.place ?: PlaceModel(), -1) }

            order.place?.let {
                order_place_work_time_text.text = TimeUtils.convertWorkTime(it.workDayStartAt, it.workDayEndAt)
                order_place_address_text.text = it.address
                order_place_title.text = it.placeTitle
            }

            if (order.status != Constants.STATUS_PAID) {
                btn_pay.enable()
                btn_pay.setOnClickListener {
                    context.startActivity(Intent(context, WebActivity::class.java).apply { putExtra("url", order.paymentUrl) })
                }
            } else {
                btn_pay.disable()
            }

            booked_container.removeAllViews()

            //Меняем порядок времени, чтобы было по увеличению
            //Для каждого значения времени в order.booking создаем view и добавляем в общий контейнер
            order.booking.reversed().forEachIndexed { index, bookingModel->
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
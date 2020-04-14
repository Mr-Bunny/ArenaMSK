package com.example.arenamsk.ui.booked.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.arenamsk.R
import com.example.arenamsk.models.CoordinatesModel
import com.example.arenamsk.models.OrderModel
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.network.models.ImageModel
import com.example.arenamsk.network.utils.AuthUtils
import com.example.arenamsk.ui.places.viewpager.PlaceViewPagerAdapter
import com.example.arenamsk.utils.TimeUtils
import com.example.arenamsk.utils.disable
import com.example.arenamsk.utils.enable
import kotlinx.android.synthetic.main.item_current_order.view.*
import kotlinx.android.synthetic.main.item_place_card.view.*

class OrdersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(order: OrderModel) {
        with(itemView) {
            order_playground_text.text = order.playground?.sport?.name ?: "Вид спорта"
            place_price_text.text = order.amount

            //TODO
        }
    }
}
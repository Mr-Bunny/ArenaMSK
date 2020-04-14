package com.example.arenamsk.ui.booked.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.arenamsk.R
import com.example.arenamsk.models.*
import com.example.arenamsk.ui.places.adapter.PlacesViewHolder

class CurrentOrdersAdapter : RecyclerView.Adapter<OrdersViewHolder>() {

    private val orders: MutableList<OrderModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_current_order, parent, false)
        return OrdersViewHolder(view)
    }

    override fun onBindViewHolder(holderScreen: OrdersViewHolder, position: Int) {
        holderScreen.bind(orders[position])
    }

    override fun getItemCount(): Int = orders.size

    fun setNewList(newList: List<OrderModel>) {
        if (orders.isNotEmpty()) orders.clear()
        orders.addAll(newList)
//        orders.addAll(mutableListOf(
//            OrderModel("1 200", "12 июня 2020 с 15:00 до 16:00", "", "", PlaygroundModel(1, SportModel("Футбол"), true)),
//            OrderModel("1 200", "12 июня 2020 с 15:00 до 16:00", "", "", PlaygroundModel(1, SportModel("Футбол"), true)),
//            OrderModel("1 200", "12 июня 2020 с 15:00 до 16:00", "", "", PlaygroundModel(1, SportModel("Футбол"), true)),
//            OrderModel("1 200", "12 июня 2020 с 15:00 до 16:00", "", "", PlaygroundModel(1, SportModel("Футбол"), true))
//        ))
        notifyDataSetChanged()
    }

}
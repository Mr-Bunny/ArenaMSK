package com.arenabooking.arenamsk.ui.booked.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arenabooking.arenamsk.R
import com.arenabooking.arenamsk.models.*

class CurrentOrdersAdapter(private val itemClickCallback: (place: PlaceModel, position: Int) -> Unit) : RecyclerView.Adapter<OrdersViewHolder>() {

    private val orders: MutableList<OrderModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_current_order, parent, false)
        return OrdersViewHolder(view)
    }

    override fun onBindViewHolder(holderScreen: OrdersViewHolder, position: Int) {
        holderScreen.bind(orders[position], itemClickCallback)
    }

    override fun getItemCount(): Int = orders.size

    fun setNewList(newList: List<OrderModel>) {
        if (orders.isNotEmpty()) orders.clear()
        orders.addAll(newList)
        notifyDataSetChanged()
    }

}
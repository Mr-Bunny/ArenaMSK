package com.arenabooking.arenamsk.ui.booking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arenabooking.arenamsk.R
import com.arenabooking.arenamsk.network.models.BookingDateModel

class PlaceBookingAdapter(private val itemClickCallback: (id: String, isSelected: Boolean) -> Unit) :
    RecyclerView.Adapter<PlaceBookingViewHolder>() {

    private val times: MutableList<BookingDateModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceBookingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_place_booking, parent, false)
        return PlaceBookingViewHolder(view)
    }

    override fun onBindViewHolder(holderScreen: PlaceBookingViewHolder, position: Int) {
        holderScreen.bind(times[position], itemClickCallback)
    }

    override fun getItemCount(): Int = times.size

    fun setNewList(newList: List<BookingDateModel>) {
        if (times.isNotEmpty()) times.clear()
        times.addAll(newList)
        notifyDataSetChanged()
    }

}
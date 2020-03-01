package com.example.arenamsk.ui.booking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.arenamsk.R
import com.example.arenamsk.models.PlaceBookingModel
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.ui.places.adapter.PlacesViewHolder

class PlaceBookingAdapter(private val itemClickCallback: () -> Unit) :
    RecyclerView.Adapter<PlaceBookingViewHolder>() {

    private val times: MutableList<PlaceBookingModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceBookingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_place_card, parent, false)
        return PlaceBookingViewHolder(view)
    }

    override fun onBindViewHolder(holderScreen: PlaceBookingViewHolder, position: Int) {
        holderScreen.bind(times[position], itemClickCallback)
    }

    override fun getItemCount(): Int = times.size

    fun setNewList(newList: List<PlaceBookingModel>) {
        if (times.isNotEmpty()) times.clear()
        times.addAll(newList)
        notifyDataSetChanged()
    }

}
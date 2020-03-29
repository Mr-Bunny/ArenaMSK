package com.example.arenamsk.ui.places.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.arenamsk.R
import com.example.arenamsk.models.CoordinatesModel
import com.example.arenamsk.models.PlaceModel

class PlacesAdapter(
    private val itemClickCallback: (place: PlaceModel, position: Int) -> Unit,
    private val mapWasClicked: (coordinatesModel: CoordinatesModel) -> Unit,
    private val itemBookingClickCallback: (place: PlaceModel) -> Unit,
    private val itemPhoneClickCallback: (phone: String) -> Unit,
    private val itemAddToFavouriteClickCallback: (
        toFavourite: Boolean,
        place: PlaceModel,
        requestAddToFavouriteFailed: (toFavourite: Boolean, place: PlaceModel) -> Unit
    ) -> Unit
) : RecyclerView.Adapter<PlacesViewHolder>() {

    val places: MutableList<PlaceModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_place_card, parent, false)
        return PlacesViewHolder(view)
    }

    override fun onBindViewHolder(holderScreen: PlacesViewHolder, position: Int) {
        holderScreen.bind(
            position,
            places[position],
            itemClickCallback,
            mapWasClicked,
            itemPhoneClickCallback,
            itemBookingClickCallback,
            itemAddToFavouriteClickCallback
        )
    }

    override fun getItemCount(): Int = places.size

    fun setNewList(newList: List<PlaceModel>) {
        if (places.isNotEmpty()) places.clear()
        places.addAll(newList)
        notifyDataSetChanged()
    }

}
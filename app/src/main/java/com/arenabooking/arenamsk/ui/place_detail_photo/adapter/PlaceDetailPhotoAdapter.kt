package com.arenabooking.arenamsk.ui.place_detail_photo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arenabooking.arenamsk.R
import com.arenabooking.arenamsk.network.models.ImageModel

class PlaceDetailPhotoAdapter : RecyclerView.Adapter<PlaceDetailPhotoViewHolder>() {

    private val photosList: MutableList<ImageModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceDetailPhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_detail_photo_item, parent, false)
        return PlaceDetailPhotoViewHolder(view)
    }

    override fun onBindViewHolder(holderScreen: PlaceDetailPhotoViewHolder, position: Int) {
        holderScreen.bind(photosList[position])
    }

    override fun getItemCount(): Int = photosList.size

    fun setNewList(newList: List<ImageModel>) {
        if (photosList.isNotEmpty()) photosList.clear()
        photosList.addAll(newList)
        notifyDataSetChanged()
    }

}
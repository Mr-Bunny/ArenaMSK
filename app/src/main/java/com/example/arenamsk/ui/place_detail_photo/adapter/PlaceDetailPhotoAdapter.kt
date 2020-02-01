package com.example.arenamsk.ui.place_detail_photo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.arenamsk.R
import com.example.arenamsk.models.PlaceModel

class PlaceDetailPhotoAdapter : RecyclerView.Adapter<PlaceDetailPhotoViewHolder>() {

    private val photosList: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceDetailPhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_detail_photo_item, parent, false)
        return PlaceDetailPhotoViewHolder(view)
    }

    override fun onBindViewHolder(holderScreen: PlaceDetailPhotoViewHolder, position: Int) {
        holderScreen.bind(photosList[position])
    }

    override fun getItemCount(): Int = photosList.size

    fun setNewList(newList: List<String>) {
        if (photosList.isNotEmpty()) photosList.clear()
        photosList.addAll(newList)
        notifyDataSetChanged()
    }

}
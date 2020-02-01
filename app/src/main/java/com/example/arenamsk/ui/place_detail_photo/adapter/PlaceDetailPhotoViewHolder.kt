package com.example.arenamsk.ui.place_detail_photo.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.arenamsk.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.place_detail_photo_item.view.*

class PlaceDetailPhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(photoUrl: String) {
        Picasso.get()
            .load(photoUrl)
            .placeholder(R.drawable.photo_background)
            .into(itemView.place_detail_photo)
    }
}
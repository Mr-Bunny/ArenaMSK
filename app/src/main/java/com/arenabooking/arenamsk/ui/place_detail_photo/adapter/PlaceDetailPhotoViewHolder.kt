package com.arenabooking.arenamsk.ui.place_detail_photo.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.arenabooking.arenamsk.R
import com.arenabooking.arenamsk.network.models.ImageModel
import com.arenabooking.arenamsk.ui.FullScreenImageActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.place_detail_photo_item.view.*

class PlaceDetailPhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(image: ImageModel) {
        Picasso.get()
            .load(image.thumbImage)
            .placeholder(R.drawable.photo_background)
            .into(itemView.place_detail_photo)

        itemView.place_detail_photo.setOnClickListener {
            itemView.context.startActivity(FullScreenImageActivity.getIntent(itemView.context, image.fullImage))
        }
    }
}
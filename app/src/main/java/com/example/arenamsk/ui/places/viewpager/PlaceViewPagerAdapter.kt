package com.example.arenamsk.ui.places.viewpager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.arenamsk.R
import com.example.arenamsk.network.models.ImageModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.place_view_pager_item.view.*

class PlaceViewPagerAdapter : RecyclerView.Adapter<PagerVH>() {

    private var images: MutableList<ImageModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH =
        PagerVH(
            LayoutInflater.from(parent.context).inflate(
                R.layout.place_view_pager_item,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: PagerVH, position: Int) = holder.itemView.run {
        Picasso.get()
            .load(images[position].fullImage)
            .placeholder(R.drawable.image_placeholder)
            .into(place_image)
    }

    fun setNewImages(newImages: MutableList<ImageModel>) {
        if (images.isNotEmpty()) images.clear()
        images.addAll(newImages)
        notifyDataSetChanged()
    }
}

class PagerVH(itemView: View) : RecyclerView.ViewHolder(itemView)
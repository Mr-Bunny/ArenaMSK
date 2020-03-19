package com.example.arenamsk.ui.places.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.arenamsk.R
import com.example.arenamsk.models.CoordinatesModel
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.network.utils.AuthUtils
import com.example.arenamsk.ui.places.viewpager.PlaceViewPagerAdapter
import com.example.arenamsk.utils.TimeUtils
import com.example.arenamsk.utils.disable
import com.example.arenamsk.utils.enable
import kotlinx.android.synthetic.main.item_place_card.view.*

class PlacesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(
        place: PlaceModel,
        itemClickCallback: (meetUpId: PlaceModel) -> Unit,
        mapWasClicked: (coordinatesModel: CoordinatesModel) -> Unit,
        itemPhoneClickCallback: (phone: String) -> Unit,
        itemBookingClickCallback: (place: PlaceModel) -> Unit,
        itemAddToFavouriteClickCallback: (
            toFavourite: Boolean,
            place: PlaceModel,
            requestAddToFavouriteFailed: (isFavourite: Boolean, place: PlaceModel) -> Unit
        ) -> Unit
    ) {
        with(itemView) {
            place_item_title.text = place.title
            place_item_description.text = place.description

            place_item_rating_bar.rating = place.rating

            place_item_feedback_count_text.text = itemView.context.getString(
                R.string.text_place_feedback_number,
                place.feedbackNumber.toString()
            )

            place_item_work_time_text.text = TimeUtils.convertWorkTime(place.workDayStartAt, place.workDayEndAt)

            place_item_address_text.text = place.address

            setUpFavouriteIcon(place.isFavourite)

            if (AuthUtils.isUserDefault()) {
                place_item_favourite_btn.disable()
            } else {
                place_item_favourite_btn.enable()
            }

            place_item_favourite_btn.setOnClickListener {
                val toFavourite = !place.isFavourite

                place.isFavourite = toFavourite
                setUpFavouriteIcon(toFavourite)
                //Делаем запрос на добавление или удаление в избранное
                itemAddToFavouriteClickCallback.invoke(
                    toFavourite,
                    place,
                    ::requestAddToFavouriteFailed
                )
            }

            place_item_show_map_btn.setOnClickListener {
                mapWasClicked.invoke(CoordinatesModel(place.latitude, place.longitude))
            }

            place_item_btn_phone.setOnClickListener {
                if (!place.phoneNumbersList.isNullOrEmpty()) {
                    itemPhoneClickCallback.invoke(place.phoneNumbersList[0])
                }
            }

            place_item_distance_text.text = itemView.context.getString(
                R.string.text_place_distance,
                place.distance.toString()
            )

            setOnClickListener { itemClickCallback.invoke(place) }

            place_item_btn_calendar.setOnClickListener { itemBookingClickCallback.invoke(place) }

            when {
                place.imagesUrl.isEmpty() -> {
                    place_item_view_pager.disable()
                    place_item_view_pager_indicator.disable()
                    return
                }
                place.imagesUrl.size == 1 -> {
                    place_item_view_pager.enable()
                    place_item_view_pager_indicator.disable()
                }
                else -> {
                    place_item_view_pager.enable()
                    place_item_view_pager_indicator.enable()
                }
            }

            val adapter = PlaceViewPagerAdapter()
            place_item_view_pager.adapter = adapter
            adapter.setNewImages(place.imagesUrl as MutableList<String>)
            place_item_view_pager_indicator.setViewPager(place_item_view_pager)
        }
    }

    /** Если запрос на добавление/удаление в избранное не прошел, если пришел, то view уже поменялась,
     *  а данные потом обновятся при повторном открытии.
     * @param isFavourite Принимаем противоположное значение того, что отправляли для запроса */
    private fun requestAddToFavouriteFailed(toFavourite: Boolean, place: PlaceModel) {
        place.isFavourite = !toFavourite
        setUpFavouriteIcon(!toFavourite)
    }

    private fun setUpFavouriteIcon(isFavourite: Boolean) {
        with(itemView.place_item_favourite_btn) {
            if (isFavourite) {
                setImageDrawable(itemView.context.resources.getDrawable(R.drawable.ic_in_favoutires_btn))
            } else {
                setImageDrawable(itemView.context.resources.getDrawable(R.drawable.ic_add_to_favourites_btn))
            }
        }
    }
}
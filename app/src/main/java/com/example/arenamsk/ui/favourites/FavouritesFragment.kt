package com.example.arenamsk.ui.favourites

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arenamsk.R
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.ui.base.BaseFragment
import com.example.arenamsk.ui.base.PlaceDialogFragment
import com.example.arenamsk.ui.places.PlacesViewModel
import com.example.arenamsk.ui.places.adapter.PlacesAdapter
import com.example.arenamsk.utils.disable
import com.example.arenamsk.utils.enable
import kotlinx.android.synthetic.main.fragment_favourites.*
import kotlinx.android.synthetic.main.fragment_places.*
import kotlinx.android.synthetic.main.places_errors_form.*

class FavouritesFragment : BaseFragment(R.layout.fragment_favourites) {

    private val placeAdapter by lazy { PlacesAdapter(::itemClickCallback, ::itemBookingClickCallback, ::addPlaceToFavourite) }

    private var placeDetailFragment: PlaceDialogFragment? = null

    private val placesViewModel by lazy {
        ViewModelProviders.of(this).get(PlacesViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()

        //LiveData с найденными площадками
        placesViewModel.getFoundedFavouritesPlacesLiveData().observe(this, Observer {
            //Показываем площадки только, если мы что-то вводили в поиск
            if (edit_text_search_favourites.text.toString().isNotEmpty()) {
                setupList(it)
            }
        })

        placesViewModel.getFavouritesPlacesLiveData().observe(viewLifecycleOwner, Observer {
            setupList(it)
        })

        //Вешаем слушатель на поле поиска площадок по названию и адресу
        edit_text_search_favourites.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(textToSearch: Editable?) {
                placesViewModel.showFilteredPlacesInFavourites(textToSearch.toString().trim())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(textToSearch: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    private fun setupList(list: MutableList<PlaceModel>?) {
        if (list.isNullOrEmpty()) {
            showPlacesNotFoundForm()
        } else {
            hidePlacesNotFoundForm()
            placeAdapter.setNewList(list)
        }
    }

    private fun initRecycler() {
        recycler_favourites.setHasFixedSize(true)
        recycler_favourites.adapter = placeAdapter
        recycler_favourites.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    private fun itemBookingClickCallback(place: PlaceModel) {
        openPlaceDetail(place, true)
    }

    private fun itemClickCallback(place: PlaceModel) {
        openPlaceDetail(place)
    }

    private fun openPlaceDetail(place: PlaceModel, openBooking: Boolean = false) {
        placeDetailFragment?.dismiss()
        placeDetailFragment = PlaceDialogFragment.getInstance(place, openBooking)
        placeDetailFragment?.show(activity!!.supportFragmentManager, PlaceDialogFragment.PLACE_DIALOG_FRAGMENT_TAG)
    }

    private fun addPlaceToFavourite(toFavourite: Boolean,
                                    place: PlaceModel,
                                    requestAddToFavouriteFailed: (toFavourite: Boolean, place: PlaceModel) -> Unit) {
        placesViewModel.addPlaceToFavourite(toFavourite, place, requestAddToFavouriteFailed)
    }

    /** Отображаем текст, что площадки не найдены */
    private fun showPlacesNotFoundForm() {
        recycler_favourites.disable()
        favourite_places_not_found_form.enable()
    }

    /** Скрываем текст, что площадки не найдены и показываем список площадок*/
    private fun hidePlacesNotFoundForm() {
        recycler_favourites.enable()
        favourite_places_not_found_form.disable()
    }
}
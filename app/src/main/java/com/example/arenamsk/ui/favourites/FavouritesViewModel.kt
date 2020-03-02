package com.example.arenamsk.ui.favourites

import androidx.lifecycle.MutableLiveData
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.ui.base.BaseViewModel

class FavouritesViewModel : BaseViewModel() {

    private var favouritesPlacesLiveData = MutableLiveData<MutableList<PlaceModel>>()

    fun loadFavouritesPlaces() {
        //favouritesPlacesLiveData.value = getTestPlaces()
    }

    fun getFavouritesPlacesLiveData() = favouritesPlacesLiveData
}
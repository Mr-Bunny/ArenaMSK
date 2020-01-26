package com.example.arenamsk.ui.favourites

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arenamsk.R
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.ui.base.BaseFragment
import com.example.arenamsk.ui.places.adapter.PlacesAdapter
import kotlinx.android.synthetic.main.fragment_favourites.*

class FavouritesFragment : BaseFragment(R.layout.fragment_favourites) {

    private val placeAdapter by lazy { PlacesAdapter(::itemClickCallback) }

    private val favouritesViewModel by lazy {
        ViewModelProviders.of(this).get(FavouritesViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()

        with(favouritesViewModel) {
            loadFavouritesPlaces()
            getFavouritesPlacesLiveData().observe(this@FavouritesFragment, Observer {
                placeAdapter.setNewList(it)
            })
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

    private fun itemClickCallback(place: PlaceModel) {
        //TODO open fragment
    }
}
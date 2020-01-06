package com.example.arenamsk.ui.places

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arenamsk.R
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.ui.base.BaseFragment
import com.example.arenamsk.ui.places.adapter.PlacesAdapter
import kotlinx.android.synthetic.main.fragment_places.*

class PlacesFragment : BaseFragment() {

    private val placeAdapter by lazy { PlacesAdapter(::itemClickCallback) }

    private val placesViewModel by lazy {
        ViewModelProviders.of(this).get(PlacesViewModel::class.java)
    }

    override fun getLayout(): Int = R.layout.fragment_places

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()

        with(placesViewModel) {
            getPlaces()
            getPlacesLiveData().observe(this@PlacesFragment, Observer {
                placeAdapter.setNewList(it)
            })
        }
    }

    private fun initRecycler() {
        recycler_places.setHasFixedSize(true)
        recycler_places.adapter = placeAdapter
        recycler_places.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    private fun itemClickCallback(place: PlaceModel) {
        //TODO open fragment
    }

}
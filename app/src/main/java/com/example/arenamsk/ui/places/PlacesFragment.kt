package com.example.arenamsk.ui.places

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arenamsk.R
import com.example.arenamsk.custom_view.TagView
import com.example.arenamsk.models.PlaceFilterModel
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.ui.base.BaseFragment
import com.example.arenamsk.ui.place_filter.PlaceFilterFragment
import com.example.arenamsk.ui.places.adapter.PlacesAdapter
import kotlinx.android.synthetic.main.fragment_places.*

class PlacesFragment : BaseFragment(), TagSelectedCallback {

    private val placeAdapter by lazy { PlacesAdapter(::itemClickCallback) }

    private val placesViewModel by lazy {
        ViewModelProviders.of(this).get(PlacesViewModel::class.java)
    }

    private var placeFilterFragment: PlaceFilterFragment? = null

    override fun getLayout(): Int = R.layout.fragment_places

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()

        placesViewModel.getPlacesLiveData().observe(this@PlacesFragment, Observer {
            placeAdapter.setNewList(it)
        })

        initTags()

        place_filter_button.setOnClickListener { openFilterFragment() }
    }

    override fun tagWasSelected(isSelected: Boolean, tagId: Int) {
        //TODO
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

    private fun initTags() {
        place_tag_container.removeAllViews()
        place_tag_container.addView(TagView(context!!, 1, "Все виды"))
        place_tag_container.addView(TagView(context!!, 2, "Баскетбол"))
        place_tag_container.addView(TagView(context!!, 3, "Футбол"))
        place_tag_container.addView(TagView(context!!, 4, "Теннис"))
        place_tag_container.addView(TagView(context!!, 5, "Волейбол"))
    }

    private fun itemClickCallback(place: PlaceModel) {
        //TODO open fragment
    }

    private fun openFilterFragment() {
        placeFilterFragment?.dismiss()
        placeFilterFragment = PlaceFilterFragment.createInstance()
        placeFilterFragment?.show(activity!!.supportFragmentManager, PlaceFilterFragment.FILTER_MODEL_TAG)
    }

}
package com.example.arenamsk.ui.places

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arenamsk.R
import com.example.arenamsk.custom_view.TagView
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.ui.base.BaseFragment
import com.example.arenamsk.ui.place_detail.PlaceDetailFragment
import com.example.arenamsk.ui.place_filter.PlaceFilterFragment
import com.example.arenamsk.ui.places.adapter.PlacesAdapter
import kotlinx.android.synthetic.main.fragment_places.*

class PlacesFragment : BaseFragment(R.layout.fragment_places), TagSelectedCallback {

    private val placeAdapter by lazy { PlacesAdapter(::itemClickCallback) }

    private val placesViewModel by lazy {
        ViewModelProviders.of(this).get(PlacesViewModel::class.java)
    }

    private var placeFilterFragment: PlaceFilterFragment? = null
    private var placeDetailFragment: PlaceDetailFragment? = null

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
        with(recycler_places) {
            setHasFixedSize(true)
            adapter = placeAdapter
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
        }
    }

    private fun initTags() {
        with(place_tag_container) {
            removeAllViews()
            addView(TagView(context!!, 1, "Все виды"))
            addView(TagView(context!!, 2, "Баскетбол"))
            addView(TagView(context!!, 3, "Мини-футбол"))
            addView(TagView(context!!, 4, "Футбол"))
            addView(TagView(context!!, 5, "Теннис"))
            addView(TagView(context!!, 6, "Волейбол"))
        }
    }

    private fun itemClickCallback(place: PlaceModel) {
        placeDetailFragment?.dismiss()
        placeDetailFragment = PlaceDetailFragment.getInstance(place)
        placeDetailFragment?.show(activity!!.supportFragmentManager, PlaceDetailFragment.PLACE_DETAIL_TAG)
    }

    private fun openFilterFragment() {
        placeFilterFragment?.dismiss()
        placeFilterFragment = PlaceFilterFragment.getInstance()
        placeFilterFragment?.show(activity!!.supportFragmentManager, PlaceFilterFragment.FILTER_MODEL_TAG)
    }

}
package com.example.arenamsk.ui.booking

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arenamsk.R
import com.example.arenamsk.ui.base.BaseFragment
import com.example.arenamsk.ui.booking.adapter.PlaceBookingAdapter
import kotlinx.android.synthetic.main.fragment_place_booking.*

class PlaceBookingFragment : BaseFragment(R.layout.fragment_place_booking) {

    private val placeBookingAdapter by lazy { PlaceBookingAdapter(::itemClickCallback) }

    private val placeBookingViewModel by lazy {
        ViewModelProviders.of(this).get(PlaceBookingViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()

        placeBookingViewModel.getPlaceBookingLiveData().observe(this, Observer {
            placeBookingAdapter.setNewList(it)
        })
    }


    private fun initRecycler() {
        with(recycler_place_booking) {
            setHasFixedSize(true)
            adapter = placeBookingAdapter
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
        }
    }


    private fun itemClickCallback() {
        //TODO сохранять кликнутый item
    }

}

package com.example.arenamsk.ui.places

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.example.arenamsk.R
import com.example.arenamsk.ui.base.BaseFragment

class PlacesFragment : BaseFragment() {

    private val placesViewModel by lazy {
        ViewModelProviders.of(this).get(PlacesViewModel::class.java)
    }

    override fun getLayout(): Int = R.layout.fragment_places

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
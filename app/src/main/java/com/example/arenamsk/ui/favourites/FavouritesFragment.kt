package com.example.arenamsk.ui.favourites

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.example.arenamsk.R
import com.example.arenamsk.ui.base.BaseFragment

class FavouritesFragment : BaseFragment() {

    private val favouritesViewModel by lazy {
        ViewModelProviders.of(this).get(FavouritesViewModel::class.java)
    }

    override fun getLayout(): Int = R.layout.fragment_favourites

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
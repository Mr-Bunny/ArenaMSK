package com.example.arenamsk.ui.booked

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.example.arenamsk.R
import com.example.arenamsk.ui.base.BaseFragment
import com.example.arenamsk.ui.booked.adapter.BookedViewPagerAdapter
import com.example.arenamsk.utils.getStatusBarHeight
import kotlinx.android.synthetic.main.fragment_booked.booked_tab_layout
import kotlinx.android.synthetic.main.fragment_booked.booked_view_pager

class BookedFragment: BaseFragment(R.layout.fragment_booked) {

    private val dispatcher by lazy { requireActivity().onBackPressedDispatcher }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dispatcher.addCallback(this) {
            findNavController().popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTopPadding(booked_tab_layout)

        with(booked_view_pager) {
            adapter = BookedViewPagerAdapter(childFragmentManager)
            booked_tab_layout.setupWithViewPager(this)
        }
    }
}
package com.arenabooking.arenamsk.ui.booked

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.arenabooking.arenamsk.R
import com.arenabooking.arenamsk.ui.base.BaseFragment
import com.arenabooking.arenamsk.ui.booked.adapter.BookedViewPagerAdapter
import com.arenabooking.arenamsk.utils.getStatusBarHeight
import kotlinx.android.synthetic.main.fragment_booked.*

/** Фрагмент для отображения viewPager и tab layout, в котором показываются текущие и прошедшие брони */
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

        btn_back.setOnClickListener { findNavController().popBackStack() }

        //setupTopPadding()

        with(booked_view_pager) {
            adapter = BookedViewPagerAdapter(childFragmentManager)
            booked_tab_layout.setupWithViewPager(this)
        }
    }

    /** Метод добавляет отступ сверху равный размеру statusBar */
    protected fun setupTopPadding() {
        val statusBarHeight = requireContext().getStatusBarHeight()
        val params = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, statusBarHeight, 0, 0)
        booked_tab_layout.layoutParams = params
    }
}
package com.example.arenamsk.ui.booked

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.example.arenamsk.R
import com.example.arenamsk.ui.base.BaseFragment
import com.example.arenamsk.utils.getStatusBarHeight
import kotlinx.android.synthetic.main.fragment_booked.booked_tab_layout

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

        setupTopPadding()
    }

    private fun setupTopPadding() {
        val statusBarHeight = requireContext().getStatusBarHeight()
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, statusBarHeight, 0, 0)
        booked_tab_layout.layoutParams = params
    }
}
package com.example.arenamsk.ui.auth.sign_up

import android.os.Bundle
import android.view.View
import com.example.arenamsk.R
import com.example.arenamsk.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_arrow.setOnClickListener { activity!!.onBackPressed() }
    }

    override fun getLayout(): Int = R.layout.fragment_sign_up
}
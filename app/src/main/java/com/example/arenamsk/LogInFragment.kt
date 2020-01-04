package com.example.arenamsk

import android.os.Bundle
import android.text.TextUtils
import android.text.TextUtils.replace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_log_in.*

class LogInFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_log_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        text_create_new_user.setOnClickListener { openSignUpFragment() }
    }

    private fun openSignUpFragment() {
        with(activity!!.supportFragmentManager.beginTransaction()) {
            replace(R.id.auth_fragment_container, SignUpFragment(), AuthActivity.FRAGMENT_SIGN_UP_TAG)
            addToBackStack(null)
            commit()
        }
    }
}
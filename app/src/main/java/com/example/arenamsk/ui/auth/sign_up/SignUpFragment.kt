package com.example.arenamsk.ui.auth.sign_up

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.arenamsk.R
import com.example.arenamsk.ui.base.BaseFragment
import com.example.arenamsk.utils.EnumUtils.SignUpStatus
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : BaseFragment() {

    private val signUpViewModel by lazy { ViewModelProviders.of(this).get(SignUpViewModel::class.java) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_arrow.setOnClickListener { activity!!.onBackPressed() }

        btn_sign_up.setOnClickListener {
            name_edit_text.clearFocus()
            email_edit_text.clearFocus()
            password_edit_text.clearFocus()
            
            signUpViewModel.startSignUp(
                name_edit_text.getText(),
                email_edit_text.getText(),
                password_edit_text.getText()
            )
        }

        signUpViewModel.getSignUpStatus().observe(viewLifecycleOwner, Observer { handleSignUpStatus(it) })
    }

    override fun getLayout(): Int = R.layout.fragment_sign_up

    private fun handleSignUpStatus(signUpStatus: SignUpStatus) {
        when (signUpStatus) {
            SignUpStatus.USERNAME_EMPTY -> {
                name_edit_text.setError(getString(R.string.text_hint_empty_field_error))
            }

            SignUpStatus.EMAIL_EMPTY -> {
                email_edit_text.setError(getString(R.string.text_hint_empty_field_error))
            }

            SignUpStatus.EMAIL_INCORRECT -> {
                email_edit_text.setError(getString(R.string.text_hint_email_incorrect_error))
            }

            SignUpStatus.PASSWORD_EMPTY -> {
                password_edit_text.setError(getString(R.string.text_hint_empty_field_error))
            }

            SignUpStatus.PASSWORD_MIN_LENGTH_ERROR -> {
                password_edit_text.setError(getString(R.string.text_hint_min_length_error))
            }

            SignUpStatus.EMAIL_EXIST -> {
                email_edit_text.setError(getString(R.string.text_hint_email_exist_error))
            }

            SignUpStatus.SIGN_UP_FAIL -> {
                showToast(getString(R.string.text_hint_sign_up_fail))
            }

            SignUpStatus.SIGN_UP_SUCCESS -> {
                //TODO open app
            }
        }
    }
}
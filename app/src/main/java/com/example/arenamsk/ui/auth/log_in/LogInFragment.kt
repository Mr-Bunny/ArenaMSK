package com.example.arenamsk.ui.auth.log_in

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.arenamsk.R
import com.example.arenamsk.ui.AuthActivity
import com.example.arenamsk.ui.auth.sign_up.SignUpFragment
import com.example.arenamsk.ui.base.BaseFragment
import com.example.arenamsk.utils.EnumUtils.LogInStatus
import kotlinx.android.synthetic.main.fragment_log_in.*

class LogInFragment : BaseFragment() {

    private val logInViewModel by lazy { ViewModelProviders.of(this).get(LogInViewModel::class.java) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        text_create_new_user.setOnClickListener { openSignUpFragment() }

        btn_log_in.setOnClickListener {
            auth_email_text_layout.clearFocus()
            auth_password_text_layout.clearFocus()
            logInViewModel.startAuth(
                auth_email_text_layout.getText(),
                auth_password_text_layout.getText()
            )
        }

        logInViewModel.getLogInStatus().observe(viewLifecycleOwner, Observer { handleLogInStatus(it) })
    }

    override fun getLayout(): Int = R.layout.fragment_log_in

    private fun handleLogInStatus(logInStatus: LogInStatus) {
        when (logInStatus) {
            LogInStatus.EMAIL_EMPTY -> {
                auth_email_text_layout.setError("Поле не может быть пустым")
            }

            LogInStatus.EMAIL_INCORRECT -> {
                auth_email_text_layout.setError("Пожалуйста введите корректный e-mail адрес")
            }

            LogInStatus.PASSWORD_EMPTY -> {
                auth_password_text_layout.setError("Поле не может быть пустым")
            }

            LogInStatus.PASSWORD_MIN_LENGTH_ERROR -> {
                auth_password_text_layout.setError("Минимальная длина 6 символов")
            }

            LogInStatus.LOG_IN_FAIL -> {

            }

            LogInStatus.LOG_IN_SUCCESS -> {
                //TODO open app
            }
        }
    }

    private fun openSignUpFragment() {
        with(activity!!.supportFragmentManager.beginTransaction()) {
            replace(
                R.id.auth_fragment_container,
                SignUpFragment(),
                AuthActivity.FRAGMENT_SIGN_UP_TAG
            )
            addToBackStack(null)
            commit()
        }
    }
}
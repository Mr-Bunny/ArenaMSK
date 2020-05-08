package com.example.arenamsk.ui.auth.log_in

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.arenamsk.R
import com.example.arenamsk.network.utils.AuthUtils
import com.example.arenamsk.ui.AuthActivity
import com.example.arenamsk.ui.app_policy.AppPolicyDialogFragment
import com.example.arenamsk.ui.app_policy.AppPolicyDialogFragment.Companion.POLICY_TAG
import com.example.arenamsk.ui.auth.password_reset.PasswordResetDialogFragment
import com.example.arenamsk.ui.auth.password_reset.PasswordResetDialogFragment.Companion.RESET_TAG
import com.example.arenamsk.ui.auth.sign_up.SignUpFragment
import com.example.arenamsk.ui.base.BaseAuthFragment
import com.example.arenamsk.utils.EnumUtils.LogInStatus
import kotlinx.android.synthetic.main.fragment_log_in.*

/** Фрагмент авторизации */
class LogInFragment : BaseAuthFragment(R.layout.fragment_log_in) {

    private val logInViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(LogInViewModel::class.java)
    }

    private var passwordResetDialogFragment: PasswordResetDialogFragment? = null
    private var acceptDialogFragment: AppPolicyDialogFragment? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        text_create_new_user.setOnClickListener { openSignUpFragment() }

        text_without_registration.setOnClickListener {
            if (AuthUtils.isUserAcceptPolitics())
                logInViewModel.skipAuth()
            else
                openPolicyScreen()
        }

        text_forget_password.setOnClickListener { openFeedbackScreen() }

        btn_log_in.setOnClickListener {
            auth_email_text_layout.clearFocus()
            auth_password_text_layout.clearFocus()

            logInViewModel.startAuth(
                auth_email_text_layout.getText().trim(),
                auth_password_text_layout.getText().trim()
            )
        }

        logInViewModel.getLogInStatus()
            .observe(viewLifecycleOwner, Observer { handleLogInStatus(it) })
    }

    private fun handleLogInStatus(logInStatus: LogInStatus) {
        when (logInStatus) {
            LogInStatus.EMAIL_EMPTY -> {
                auth_email_text_layout.setError(getString(R.string.text_hint_empty_field_error))
            }

            LogInStatus.EMAIL_INCORRECT -> {
                auth_email_text_layout.setError(getString(R.string.text_hint_email_incorrect_error))
            }

            LogInStatus.PASSWORD_EMPTY -> {
                auth_password_text_layout.setError(getString(R.string.text_hint_empty_field_error))
            }

            LogInStatus.PASSWORD_MIN_LENGTH_ERROR -> {
                auth_password_text_layout.setError(getString(R.string.text_hint_min_length_error))
            }

            LogInStatus.LOG_IN_FAIL -> {
                showToast(getString(R.string.text_hint_log_in_fail))
            }

            LogInStatus.LOG_IN_SUCCESS -> {
                openApp(activity as AuthActivity)
            }

            LogInStatus.NETWORK_OFFLINE -> {
                showNetworkOfflineError()
            }

            LogInStatus.BAD_CREDENTIALS -> {
                showToast(R.string.bad_credentials_error)
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

    private fun openPolicyScreen() {
        acceptDialogFragment?.dismiss()
        acceptDialogFragment = AppPolicyDialogFragment.getInstance()
        acceptDialogFragment?.show(
            activity!!.supportFragmentManager,
            POLICY_TAG
        )
    }

    private fun openFeedbackScreen() {
        passwordResetDialogFragment?.dismiss()
        passwordResetDialogFragment = PasswordResetDialogFragment.getInstance()
        passwordResetDialogFragment?.show(
            activity!!.supportFragmentManager,
            RESET_TAG
        )
    }
}
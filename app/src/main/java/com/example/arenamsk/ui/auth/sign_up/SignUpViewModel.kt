package com.example.arenamsk.ui.auth.sign_up

import android.util.Patterns
import com.example.arenamsk.ui.auth.AuthConst.PASSWORD_LENGTH
import com.example.arenamsk.ui.base.BaseViewModel
import com.example.arenamsk.utils.EnumUtils.SignUpStatus
import com.example.arenamsk.utils.SingleLiveEvent

class SignUpViewModel : BaseViewModel() {

    private val signUpStatus = SingleLiveEvent<SignUpStatus>()

    fun getSignUpStatus() = signUpStatus

    fun startSignUp(name: String, email: String, password: String) {
        if (checkForError(name, email, password)) return

        //TODO sign up
    }

    private fun checkForError(name: String, email: String, password: String): Boolean {
        when {
            name.isEmpty() -> {
                signUpStatus.value = SignUpStatus.USERNAME_EMPTY
                return true
            }

            email.isEmpty() -> {
                signUpStatus.value = SignUpStatus.EMAIL_EMPTY
                return true
            }

            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                signUpStatus.value = SignUpStatus.EMAIL_INCORRECT
                return true
            }

            password.isEmpty() -> {
                signUpStatus.value = SignUpStatus.PASSWORD_EMPTY
                return true
            }

            password.length < PASSWORD_LENGTH -> {
                signUpStatus.value = SignUpStatus.PASSWORD_MIN_LENGTH_ERROR
                return true
            }

            else -> {
                return false
            }
        }
    }

}
package com.example.arenamsk.ui.auth.log_in

import android.util.Patterns
import com.example.arenamsk.ui.auth.AuthConst.PASSWORD_LENGTH
import com.example.arenamsk.ui.base.BaseViewModel
import com.example.arenamsk.utils.EnumUtils.LogInStatus
import com.example.arenamsk.utils.SingleLiveEvent

class LogInViewModel : BaseViewModel() {

    private val logInStatus = SingleLiveEvent<LogInStatus>()

    fun getLogInStatus() = logInStatus

    //Try to make registration request
    fun startAuth(email: String, password: String) {
        if (checkForError(email, password)) return

        //TODO auth
    }

    //returns true if something is empty or incorrect and false if all all right
    private fun checkForError(email: String, password: String): Boolean {
        when {
            email.isEmpty() -> {
                logInStatus.value = LogInStatus.EMAIL_EMPTY
                return true
            }

            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                logInStatus.value = LogInStatus.EMAIL_INCORRECT
                return true
            }

            password.isEmpty() -> {
                logInStatus.value = LogInStatus.PASSWORD_EMPTY
                return true
            }

            password.length < PASSWORD_LENGTH -> {
                logInStatus.value = LogInStatus.PASSWORD_MIN_LENGTH_ERROR
                return true
            }

            else -> {
                return false
            }
        }
    }

}
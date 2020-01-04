package com.example.arenamsk.utils

object EnumUtils {

    enum class SignUpStatus {
        USERNAME_EMPTY,
        EMAIL_EMPTY,
        EMAIL_INCORRECT,
        PASSWORD_EMPTY,
        PASSWORD_MIN_LENGTH_ERROR,
        EMAIL_EXIST,
        SIGN_UP_FAIL,
        SIGN_UP_SUCCESS
    }

    enum class LogInStatus {
        EMAIL_EMPTY,
        EMAIL_INCORRECT,
        PASSWORD_EMPTY,
        PASSWORD_MIN_LENGTH_ERROR,
        LOG_IN_FAIL,
        LOG_IN_SUCCESS
    }
}
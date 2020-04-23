package com.example.arenamsk.utils

/** Константы */
object Constants {
    //Длина пароля
    const val PASSWORD_LENGTH = 6
    //Промежуток времени, в течение которого нужно нажать, чтобы выйти из приложения
    const val DOUBLE_CLICK_DELAY = 2000L
    //Имя пользователя по умолчанию
    const val DEFAULT_USER_NAME = "Пользователь"

    /** Auth errors */
    const val BAD_CREDENTIALS = "Unauthorized: Bad credentials"
    const val EMAIL_ALREADY_EXISTS = "Email is already in use!"
}
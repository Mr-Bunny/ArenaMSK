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
        SIGN_UP_SUCCESS,
        NETWORK_OFFLINE
    }

    enum class LogInStatus {
        EMAIL_EMPTY,
        EMAIL_INCORRECT,
        PASSWORD_EMPTY,
        PASSWORD_MIN_LENGTH_ERROR,
        LOG_IN_FAIL,
        BAD_CREDENTIALS,
        LOG_IN_SUCCESS,
        NETWORK_OFFLINE
    }

    enum class GetPlacesStatus {
        LOAD_PLACES,
        NOT_FOUND,
        REQUEST_ERROR,
        NETWORK_OFFLINE
    }

    enum class Sports(val type: String) {
        SPORT_ALL("Все виды"),
        SPORT_FOOTBALL("Футбол"),
        SPORT_MINI_FOOTBALL("Мини-футбол"),
        SPORT_VOLLEYBALL("Волейбол"),
        SPORT_BASKETBALL("Баскетбол"),
        SPORT_TENNIS("Теннис")
    }

    //Конвертируем константы в список видов спорта
    fun getSportList() = mutableListOf<String>().apply {
        Sports.values().forEach { add(it.type) }
    }
}
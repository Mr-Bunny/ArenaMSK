package com.example.arenamsk.ui.base

import com.example.arenamsk.datasources.LocalDataSource
import com.example.arenamsk.network.utils.AuthUtils
import com.example.arenamsk.room.tables.User
import com.example.arenamsk.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseAuthViewModel: BaseViewModel() {

    /** Пропускаем авторизацию */
    open fun skipAuth() {
        launch(Dispatchers.IO) {
            //Сохраняем в БД данные пользователя
            User(firstName = Constants.DEFAULT_USER_NAME).also {
                LocalDataSource.saveUserData(it)
            }

            //Сохраняем токены и ставим флаг, что авторизированы
            with(AuthUtils) {
                saveAuthToken("")
                saveRefreshToken("")
                setUserIsDefault(true)
                setUserIsAuthorized(true)
            }
        }
    }
}
package com.arenabooking.arenamsk.ui.base

import com.arenabooking.arenamsk.datasources.LocalDataSource
import com.arenabooking.arenamsk.network.utils.AuthUtils
import com.arenabooking.arenamsk.room.tables.User
import com.arenabooking.arenamsk.utils.Constants
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

            //Ставим флаг, что авторизированы
            with(AuthUtils) {
                saveAuthToken("")
                saveRefreshToken("")
                setUserIsDefault(true)
                setUserIsAuthorized(true)
            }
        }
    }
}
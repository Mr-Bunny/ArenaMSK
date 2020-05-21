package com.arenabooking.arenamsk.datasources

import com.arenabooking.arenamsk.App
import com.arenabooking.arenamsk.room.ArenaDatabase
import com.arenabooking.arenamsk.room.tables.User

/** DataSource отвечающий за работу с данными, которые хронятся в локальной БД */
object LocalDataSource {

    private val roomStorage by lazy { ArenaDatabase.getInstance(App.appContext()) }

    /** Получение текущего пользователя в базе */
    suspend fun getUserData() = roomStorage.userDao().getAll().firstOrNull()

    /** Созранение пользователя в БД */
    suspend fun saveUserData(user: User) {
        with(roomStorage.userDao()) {
            deleteAll()
            insert(user)
        }
    }

    /** Обновление текущего пользователя в БД */
    suspend fun updateUserData(user: User) = roomStorage.userDao().update(user)

    /** Поулчение списка станций метро */
    suspend fun getLocalSubwayList() = roomStorage.subwaysDao().getAll()
}
package com.example.arenamsk.datasources

import com.example.arenamsk.App
import com.example.arenamsk.room.ArenaDatabase
import com.example.arenamsk.room.tables.User

object LocalDataSource {

    private val roomStorage by lazy { ArenaDatabase.getInstance(App.appContext()) }

    suspend fun getUserData() = roomStorage.userDao().getAll().firstOrNull()

    suspend fun saveUserData(user: User) {
        with(roomStorage.userDao()) {
            deleteAll()
            insert(user)
        }
    }

    suspend fun updateUserData(user: User) = roomStorage.userDao().update(user)

    suspend fun getLocalSubwayList() = roomStorage.subwaysDao().getAll()
}
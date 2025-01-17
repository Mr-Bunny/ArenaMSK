package com.arenabooking.arenamsk.room.dao

import androidx.room.*
import com.arenabooking.arenamsk.room.tables.User

/** Dao для работы с объектом пользователя в БД */
@Dao
interface UserDataDao {
    @Query("DELETE from userTable")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<User>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: User)

    @Query("SELECT * from userTable")
    suspend fun getAll(): List<User>

    @Update
    suspend fun update(item: User)
}

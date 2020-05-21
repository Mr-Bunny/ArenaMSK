package com.arenabooking.arenamsk.room.dao

import androidx.room.*
import com.arenabooking.arenamsk.room.tables.Subway

/** Dao для работы со списком метро в БД */
@Dao
interface SubwaysDao {
    @Query("DELETE from subwaysTable")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<Subway>)

    @Query("SELECT * from subwaysTable")
    suspend fun getAll(): List<Subway>
}

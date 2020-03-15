package com.example.arenamsk.room.dao

import androidx.room.*
import com.example.arenamsk.room.tables.Subway

@Dao
interface SubwaysDao {
    @Query("DELETE from subwaysTable")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<Subway>)

    @Query("SELECT * from subwaysTable")
    suspend fun getAll(): List<Subway>
}

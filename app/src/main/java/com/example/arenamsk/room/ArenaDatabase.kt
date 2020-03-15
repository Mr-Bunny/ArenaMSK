package com.example.arenamsk.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.arenamsk.room.ArenaDatabase.Companion.DB_VERSION
import com.example.arenamsk.room.dao.SubwaysDao
import com.example.arenamsk.room.dao.UserDataDao
import com.example.arenamsk.room.tables.Subway
import com.example.arenamsk.room.tables.User

@Database(
    entities = [User::class, Subway::class],
    version = DB_VERSION
)
abstract class ArenaDatabase : RoomDatabase() {
    companion object {
        private const val DATABASE_NAME = "arena_db"
        const val DB_VERSION = 1

        @Volatile
        private var instance: ArenaDatabase? = null

        @Synchronized
        fun getInstance(ctx: Context): ArenaDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    ctx.applicationContext,
                    ArenaDatabase::class.java,
                    DATABASE_NAME
                ).build()
            }

            return instance!!
        }

        fun destroyInstance() {
            instance = null
        }
    }


    abstract fun userDao(): UserDataDao

    abstract fun subwaysDao(): SubwaysDao
}

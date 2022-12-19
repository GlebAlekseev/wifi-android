package com.example.wifitrilateration.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.glebalekseevjk.yasmrhomework.data.local.dao.RouterDao
import com.glebalekseevjk.yasmrhomework.data.local.model.RouterDbModel

@Database(entities = [RouterDbModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "wifi-trilateration-database"
    }

    abstract fun routerDao(): RouterDao
}
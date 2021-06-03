package com.algebra.wheathertask.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.algebra.wheathertask.model.HistoryOfCity

@Database(entities = [HistoryOfCity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun historyOfCityDao(): HistoryOfCityDao
}
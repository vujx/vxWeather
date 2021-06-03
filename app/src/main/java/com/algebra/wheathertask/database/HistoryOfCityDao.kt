package com.algebra.wheathertask.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.algebra.wheathertask.model.HistoryOfCity
import androidx.room.Query

@Dao
interface HistoryOfCityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCity(city: HistoryOfCity)

    @Query("DELETE FROM historyOfCities WHERE id = :id")
    fun deleteLastCity(id: Int)

    @Query("SELECT * FROM historyOfCities")
    suspend fun getAllCities(): List<HistoryOfCity>
}
package com.algebra.wheathertask.repository

import com.algebra.wheathertask.database.AppDatabase
import com.algebra.wheathertask.di.DatabaseIOExecutor
import com.algebra.wheathertask.model.HistoryOfCity
import java.util.concurrent.Executor
import javax.inject.Inject

class HistoryOfCitiesRepository @Inject constructor(private val appDatabase: AppDatabase,
        @DatabaseIOExecutor private val databaseExecutor: Executor
){
    private val historyOfCityDao = appDatabase.historyOfCityDao()

    fun addCityRepo(city: HistoryOfCity) {
        databaseExecutor.execute {
            historyOfCityDao.insertCity(city)
        }
    }

    fun deleteCityRepo(id: Int){
        databaseExecutor.execute {
            historyOfCityDao.deleteLastCity(id)
        }
    }

    suspend fun getAllCitiesRepo(): List<HistoryOfCity> {
        return appDatabase.historyOfCityDao().getAllCities()
    }
}
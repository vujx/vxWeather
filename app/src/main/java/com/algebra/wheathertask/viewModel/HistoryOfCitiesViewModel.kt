package com.algebra.wheathertask.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.algebra.wheathertask.model.City
import com.algebra.wheathertask.model.HistoryOfCity
import com.algebra.wheathertask.repository.HistoryOfCitiesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryOfCitiesViewModel @Inject constructor(private val repo: HistoryOfCitiesRepository): ViewModel() {

    var historyCities: MutableLiveData<List<HistoryOfCity>> = MutableLiveData()

    suspend fun getAllHistoryOfCities(): List<HistoryOfCity> = repo.getAllCitiesRepo()


    fun insertCity(id: Int, cityName: String, countryName: String){
        val historyOfCity = HistoryOfCity(id, cityName, countryName)
        repo.addCityRepo(historyOfCity)
    }

    fun deleteCity(id: Int){
        repo.deleteCityRepo(id)
    }


}
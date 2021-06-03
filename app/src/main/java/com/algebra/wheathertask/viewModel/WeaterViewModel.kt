package com.algebra.wheathertask.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.algebra.wheathertask.model.City
import com.algebra.wheathertask.model.Weather
import com.algebra.wheathertask.repository.SearchingRepository
import com.algebra.wheathertask.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repo: WeatherRepository, private val repoSearching: SearchingRepository) : ViewModel() {

    var errorObser: MutableLiveData<String> = MutableLiveData()
    var weatherProperty: MutableLiveData<Weather> = MutableLiveData()
    var citiesProperty: MutableLiveData<City> = MutableLiveData()

    fun getWeaterProperties(cityName: String) {
        repo.getWeatherProperties(
            cityName,
            object : WeatherRepository.OnGetWeatherPropertiesFinishedListener {
                override fun onSuccess(weather: Weather) {
                    weatherProperty.postValue(weather)
                }

                override fun onFailure(error: String) {
                    errorObser.postValue(error)
                }
            })
    }

    fun getSeachingCities(limit: String, namePrefix: String){
        repoSearching.searchingCities(limit, namePrefix, object: SearchingRepository.OnGetListOfCitiesFinishedListner{
            override fun onSuccess(list: City) {
                citiesProperty.postValue(list)
            }

            override fun onFailure(error: String) {
                errorObser.postValue(error)
            }
        })
    }

}
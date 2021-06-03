package com.algebra.wheathertask.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.algebra.wheathertask.constants.Constants
import com.algebra.wheathertask.di.SocialNetworkRetrofit
import com.algebra.wheathertask.model.City
import com.algebra.wheathertask.model.Weather
import com.algebra.wheathertask.networking.SocialNetworkService
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val apiServer: SocialNetworkService){

    interface OnGetWeatherPropertiesFinishedListener{
        fun onSuccess(weather: Weather)
        fun onFailure(error: String)
    }

    fun getWeatherProperties(cityName: String, listener: OnGetWeatherPropertiesFinishedListener){

        val observer = object: Observer<Weather>{
            override fun onSubscribe(d: Disposable) {
                Log.d("ISPIS", d.toString())
            }

            override fun onNext(t: Weather) {
                listener.onSuccess(t)
            }

            override fun onError(e: Throwable) {
                listener.onFailure(e.toString())
            }

            override fun onComplete() {
                Log.d("OnComplete", "Oncplomplete")
            }
        }

        apiServer.getWeatherProperties(cityName, Constants.API_KEY)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer)
    }

    fun getRxWeather(cityName: String) = apiServer.getWeatherProperties(cityName, Constants.API_KEY)

}
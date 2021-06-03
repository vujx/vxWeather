package com.algebra.wheathertask.repository

import android.util.Log
import com.algebra.wheathertask.model.City
import com.algebra.wheathertask.networking.SearchingFilterNetworkService
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class SearchingRepository @Inject constructor(private val apiServerSearching: SearchingFilterNetworkService) {
        interface OnGetListOfCitiesFinishedListner{
            fun onSuccess(list: City)
            fun onFailure(error: String)
        }

        fun searchingCities(limit: String, namePrefix: String, listener: OnGetListOfCitiesFinishedListner){
            val observer = object: Observer<City> {
                override fun onComplete() {
                    Log.d("ONcompleteSearching", "Oncomplete")
                }

                override fun onSubscribe(d: Disposable) {
                    Log.d("Onsubscribe", "Searvhin")
                }

                override fun onNext(t: City) {
                    listener.onSuccess(t)
                }

                override fun onError(e: Throwable) {
                    listener.onFailure(e.message.toString())
                }
            }
            apiServerSearching.getCities(limit, namePrefix)
                .debounce(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        }

}
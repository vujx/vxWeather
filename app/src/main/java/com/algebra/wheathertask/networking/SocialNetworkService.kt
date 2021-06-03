package com.algebra.wheathertask.networking

import com.algebra.wheathertask.model.Weather
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call

interface SocialNetworkService {

    @GET("weather")
    fun getWeatherProperties(@Query("q") cityName: String,
                             @Query("appid") apikey: String): Observable<Weather>


}
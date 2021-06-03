package com.algebra.wheathertask.networking

import com.algebra.wheathertask.model.City
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface SearchingFilterNetworkService {

    @Headers(
        "x-rapidapi-key: fd9d9b3dcbmsh0cfa8135b3f0556p17f240jsn31cd017577da",
        "x-rapidapi-host: wft-geo-db.p.rapidapi.com",
        "useQueryString: true")
    @GET("cities")
    fun getCities(
        @Query("limit") limit: String,
        @Query("namePrefix") namePrefix: String
    ): Observable<City>

}
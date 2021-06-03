package com.algebra.wheathertask.model

import com.squareup.moshi.Json

data class City(
   @field: Json(name = "data")
   val data: List<CitiesNames>
)

data class CitiesNames(
    @field: Json(name = "city")
    val city: String,

    @field: Json(name = "country")
    val country: String
)

package com.algebra.wheathertask.model

import com.squareup.moshi.Json

data class Weather(
    @field: Json(name = "weather")
    val weather: List<WeatherCondition>,

    @field:Json(name = "main")
    val main: WeatherChanges,

    @field:Json(name = "wind")
    val wind: Wind,

    @field:Json(name = "id")
    val id: Int,

    @field:Json(name = "name")
    val name: String,

    @field: Json(name = "cod")
    val cod: Int,

    @field:Json(name = "sys")
    val sys: Country
)

data class Country(
    @field:Json(name = "country")
    val country: String
)

data class WeatherCondition(
    @field:Json(name = "id")
    val id: Int,

    @field:Json(name = "main")
    val main: String,

    @field:Json(name = "description")
    val description: String
)

data class WeatherChanges(
    @field:Json(name = "temp")
    val temp: Double,

    @field:Json(name = "pressure")
    val pressure: Double,

    @field:Json(name = "humidity")
    val humidity: Int,

    @field:Json(name = "temp_min")
    val temp_min:Double,

    @field:Json(name = "temp_max")
    val temp_max: Double,

    @field:Json(name = "feels_like")
    val feels_like: Double

)

data class Wind(
    @field:Json(name = "speed")
    val speed: Double,

    @field:Json(name = "deg")
    val deg: Double
)
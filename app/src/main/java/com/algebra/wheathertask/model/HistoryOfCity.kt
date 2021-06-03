package com.algebra.wheathertask.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "historyOfCities")
data class HistoryOfCity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "city")
    val city: String,

    @ColumnInfo(name = "country")
    val country: String
)
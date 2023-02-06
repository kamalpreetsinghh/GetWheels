package com.cleverlycode.getwheels.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cars")
data class CarEntity(
    @PrimaryKey()
    val id: String = "",
    val name: String = "",
    val color: String = "",
    val company: String = "",
    val price: Int = 0,
    val year: Int = 0,
    val ratings: Double = 0.0,
    val numberOfTrips: Int = 0,
    val hostId: String = "",
    var isFavorite: Boolean = false,
    val acceleration: Double = 0.0,
    val hasManualTransmission: Boolean = false,
    val location: String = "",
    val range: Int = 0,
    val topSpeed: Int = 0
)
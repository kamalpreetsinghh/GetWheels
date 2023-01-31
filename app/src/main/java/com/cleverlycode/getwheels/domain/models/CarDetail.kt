package com.cleverlycode.getwheels.domain.models

import com.google.firebase.storage.StorageReference

data class CarDetail(
    var id: String = "",
    val name: String = "",
    val color: String = "",
    val company: String = "",
    val price: Int = 0,
    val year: Int = 0,
    val ratings: Double = 0.0,
    val numberOfTrips: Int = 0,
    val hostId: String? = null,
    var imageRef: StorageReference? = null,
    var imageLocation: String = "",
    val isFavorite: Boolean = false,
    val acceleration: Double = 0.0,
    val topSpeed: Int = 0,
    val range: Int = 0,
    val hasManualTransmission: Boolean = false,
    val location: String = ""
)
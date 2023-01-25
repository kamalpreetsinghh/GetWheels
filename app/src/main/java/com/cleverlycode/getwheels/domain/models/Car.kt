package com.cleverlycode.getwheels.domain.models

import com.google.firebase.storage.StorageReference

data class Car(
    var id: String? = null,
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
    var isFavorite: Boolean = false
)


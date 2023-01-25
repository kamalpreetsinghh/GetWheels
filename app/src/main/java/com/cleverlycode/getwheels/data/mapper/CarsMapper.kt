package com.cleverlycode.getwheels.data.mapper

import com.cleverlycode.getwheels.data.local.CarEntity
import com.cleverlycode.getwheels.domain.models.Car
import com.cleverlycode.getwheels.domain.models.CarDetail

fun CarEntity.toCar(): Car {
    return Car(
        id = id,
        name = name,
        color = color,
        company = company,
        price = price,
        year = year,
        ratings = ratings,
        numberOfTrips = numberOfTrips,
        hostId = hostId,
        imageLocation = imageLocation,
        isFavorite = isFavorite
    )
}

fun CarEntity.toCarDetail(): CarDetail {
    return CarDetail(
        id = id,
        name = name,
        color = color,
        company = company,
        price = price,
        year = year,
        ratings = ratings,
        numberOfTrips = numberOfTrips,
        hostId = hostId,
        imageLocation = imageLocation,
        isFavorite = isFavorite,
        acceleration = acceleration,
        topSpeed = topSpeed,
        range = range,
        hasManualTransmission = hasManualTransmission,
        location = location
    )
}

fun CarDetail.toCarEntity(): CarEntity {
    return CarEntity(
        id = id,
        name = name,
        color = color,
        company = company,
        price = price,
        year = year,
        ratings = ratings,
        numberOfTrips = numberOfTrips,
        hostId = hostId ?: "",
        imageLocation = imageLocation,
        isFavorite = isFavorite,
        acceleration = acceleration,
        topSpeed = topSpeed,
        range = range,
        hasManualTransmission = hasManualTransmission,
        location = location
    )
}



package com.cleverlycode.getwheels.domain.models

import java.time.LocalDate

data class BookingDetails(
    val userId: String? = null,
    val carId: String? = null,
    val fromDate: LocalDate = LocalDate.now(),
    val toDate: LocalDate = LocalDate.now(),
    val location: String = "",
    val subTotal: Int = 0,
    val tax: Double = 0.0,
    val totalPrice: Double = 0.0
)

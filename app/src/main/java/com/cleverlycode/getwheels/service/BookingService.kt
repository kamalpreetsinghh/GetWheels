package com.cleverlycode.getwheels.service

import com.cleverlycode.getwheels.domain.models.BookingDetails

interface BookingService {
    suspend fun getBookingDetails(userId: String): List<BookingDetails>
    suspend fun saveBookingDetails(bookingDetails: BookingDetails)
}
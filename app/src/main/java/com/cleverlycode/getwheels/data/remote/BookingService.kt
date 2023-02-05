package com.cleverlycode.getwheels.data.remote

import com.cleverlycode.getwheels.domain.models.BookingDetails

interface BookingService {
    suspend fun getBookingDetails(userId: String): List<BookingDetails>
    suspend fun saveBookingDetails(bookingDetails: BookingDetails)
}
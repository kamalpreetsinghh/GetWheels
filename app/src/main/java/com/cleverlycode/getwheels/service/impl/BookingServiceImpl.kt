package com.cleverlycode.getwheels.service.impl

import com.cleverlycode.getwheels.domain.models.BookingDetails
import com.cleverlycode.getwheels.data.remote.BookingService
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import java.time.ZoneId
import java.util.*
import javax.inject.Inject

class BookingServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : BookingService {
    override suspend fun getBookingDetails(userId: String): List<BookingDetails> =
        firestore.collection(
            BOOKINGS_COLLECTION
        )
            .whereEqualTo("userId", userId)
            .get()
            .await().map {
                it.toObject()
            }

    override suspend fun saveBookingDetails(bookingDetails: BookingDetails) {
        val fromDateTimestamp = Timestamp(
            Date.from(
                bookingDetails.fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
            )
        )
        val toDateTimestamp = Timestamp(
            Date.from(
                bookingDetails.toDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
            )
        )

        val carBookingDetails = hashMapOf(
            "userId" to bookingDetails.carId,
            "carId" to bookingDetails.carId,
            "fromDate" to fromDateTimestamp,
            "toDate" to toDateTimestamp
        )

        firestore.collection(BOOKINGS_COLLECTION)
            .document()
            .set(carBookingDetails).await()
    }

    companion object {
        private const val BOOKINGS_COLLECTION = "bookings"
    }
}
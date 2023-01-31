package com.cleverlycode.getwheels.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cleverlycode.getwheels.domain.models.BookingDetails
import com.cleverlycode.getwheels.service.AccountService
import com.cleverlycode.getwheels.service.BookingService
import com.cleverlycode.getwheels.service.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarBookingViewModel @Inject constructor(
    private val bookingService: BookingService,
    accountService: AccountService,
    logService: LogService
) : GetWheelsViewModel(logService) {
    private val _bookingDetails =
        MutableLiveData(BookingDetails(userId = accountService.currentUserId))
    val bookingDetails: LiveData<BookingDetails> get() = _bookingDetails

    fun setBookingDetails(bookingDetails: BookingDetails) {
        _bookingDetails.value = _bookingDetails.value?.copy(
            carId = bookingDetails.carId,
            fromDate = bookingDetails.fromDate,
            toDate = bookingDetails.toDate
        )
    }

    fun saveBookingDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            bookingDetails.value?.let {
                bookingService.saveBookingDetails(bookingDetails = it)
            }
        }
    }
}
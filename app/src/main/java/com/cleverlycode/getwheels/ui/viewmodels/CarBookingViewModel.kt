package com.cleverlycode.getwheels.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cleverlycode.getwheels.data.remote.AccountService
import com.cleverlycode.getwheels.data.remote.BookingService
import com.cleverlycode.getwheels.data.remote.LogService
import com.cleverlycode.getwheels.domain.models.BookingDetails
import com.cleverlycode.getwheels.domain.models.CarDetail
import com.cleverlycode.getwheels.domain.repositories.CarsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarBookingViewModel @Inject constructor(
    private val carsRepository: CarsRepository,
    private val bookingService: BookingService,
    accountService: AccountService,
    logService: LogService
) : GetWheelsViewModel(logService) {
    private val _bookingDetails = MutableLiveData(
        BookingDetails(userId = accountService.currentUserId)
    )
    val bookingDetails: LiveData<BookingDetails> get() = _bookingDetails

    private val _carDetails = MutableLiveData<CarDetail>()
    val carDetails: LiveData<CarDetail> get() = _carDetails

    init {
        bookingDetails.value?.carId?.let { getCarDetails(carId = it) }
    }

    private fun getCarDetails(carId: String) {
        viewModelScope.launch {
            _carDetails.value = carsRepository.getCarDetails(carId = carId)
            _bookingDetails.value = _bookingDetails.value?.copy(
                location = carsRepository.getCarDetails(carId = carId)?.location ?: ""
            )
        }
    }

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
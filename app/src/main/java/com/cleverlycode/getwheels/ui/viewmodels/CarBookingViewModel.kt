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
import java.time.LocalDate
import java.time.Period
import javax.inject.Inject

@HiltViewModel
class CarBookingViewModel @Inject constructor(
    private val carsRepository: CarsRepository,
    private val bookingService: BookingService,
    accountService: AccountService,
    logService: LogService
) : GetWheelsViewModel(logService) {
    private val taxPercent = 0.13
    private val _bookingDetails = MutableLiveData(
        BookingDetails(userId = accountService.currentUserId)
    )
    val bookingDetails: LiveData<BookingDetails> get() = _bookingDetails

    private val _carDetails = MutableLiveData<CarDetail>()
    val carDetails: LiveData<CarDetail> get() = _carDetails

    fun setBookingDetails(carId: String, fromDate: LocalDate, toDate: LocalDate) {
        viewModelScope.launch {
            val carRetrievedDetails = carsRepository.getCarDetails(carId = carId)
            carRetrievedDetails?.let {carInfo ->
                _carDetails.value = carInfo
                val subTotal = calculateSubTotal(carInfo.price, fromDate, toDate)
                val tax = calculateTax(subTotal)
                _bookingDetails.value = _bookingDetails.value?.copy(
                    carId = carId,
                    fromDate = fromDate,
                    toDate = toDate,
                    location = carInfo.location,
                    subTotal = subTotal,
                    tax = tax,
                    totalPrice = subTotal + tax
                )
            }
        }
    }

    fun saveBookingDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            bookingDetails.value?.let {
                bookingService.saveBookingDetails(bookingDetails = it)
            }
        }
    }

    fun navigateBack(navigate: () -> Unit) {
        navigate();
    }

    private fun calculateSubTotal(price: Int, startDate: LocalDate, endDate: LocalDate) =
        price * Period.between(startDate, endDate).days

    private fun calculateTax(subTotal: Int) = subTotal * taxPercent

}
package com.cleverlycode.getwheels.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.cleverlycode.getwheels.data.remote.CarsService
import com.cleverlycode.getwheels.domain.models.CarDetail
import com.cleverlycode.getwheels.domain.repositories.CarsRepository
import com.cleverlycode.getwheels.service.LogService
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class CarDetailsViewModel @Inject constructor(
    private val carsRepository: CarsRepository,
    private val carsService: CarsService,
    logService: LogService
) : GetWheelsViewModel(logService) {
    private val _carDetails = MutableLiveData<CarDetail>()
    val carDetails: LiveData<CarDetail> get() = _carDetails

    val imageRefs = MutableLiveData<List<StorageReference>>()

    private val _fromDate = MutableLiveData<LocalDate>()
    val fromDate: LiveData<LocalDate> get() = _fromDate

    private val _toDate = MutableLiveData<LocalDate>()
    val toDate: LiveData<LocalDate> get() = _toDate

    val showDates = MutableLiveData(false)

    fun setFromDate(milliseconds: Long) {
        _fromDate.value = Instant.ofEpochMilli(milliseconds).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    fun setToDate(milliseconds: Long) {
        _toDate.value = Instant.ofEpochMilli(milliseconds).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    fun getCarDetails(carId: String) {
        viewModelScope.launch() {
            _carDetails.value = carsRepository.getCarDetails(carId = carId)
        }

        viewModelScope.launch() {
            imageRefs.value = carsService.getCarsPictureRefList(carId)
        }
    }

    fun navigateToCarBooking(action: NavDirections, navigate: (NavDirections) -> Unit) {
        navigate(action)
    }
}
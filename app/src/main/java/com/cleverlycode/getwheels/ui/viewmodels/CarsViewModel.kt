package com.cleverlycode.getwheels.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cleverlycode.getwheels.domain.models.Car
import com.cleverlycode.getwheels.domain.repositories.CarsRepository
import com.cleverlycode.getwheels.service.AccountService
import com.cleverlycode.getwheels.service.FavoritesService
import com.cleverlycode.getwheels.service.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarsViewModel @Inject constructor(
    private val accountService: AccountService,
    private val favoritesService: FavoritesService,
    private val carsRepository: CarsRepository,
    logService: LogService
) : GetWheelsViewModel(logService) {
    val cars = MutableLiveData(emptyList<Car>())

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isSyncing = MutableLiveData(false)
    val isSyncing: LiveData<Boolean> get() = _isSyncing

    private var isUserLoggedIn = accountService.currentUser != null

    init {
        getCarsStream()
    }

    private fun getCarsStream() {
        viewModelScope.launch {
            carsRepository.getCarsStream().collectLatest { carsList ->
                _isLoading.value = false
                if (carsList.isEmpty()) {
                    carsRepository.syncWithRemoteDataSource()
                    _isSyncing.value = true
                } else {
                    cars.value = carsList
                }
            }
        }
    }

    fun favButtonClick(carId: String, isFavorite: Boolean) {
        viewModelScope.launch {
            if (isFavorite) {
                favoritesService.addFavorite(
                    carId = carId,
                    userId = accountService.currentUserId
                )
            } else {
                favoritesService.removeFavorite(
                    carId = carId,
                    userId = accountService.currentUserId
                )
            }
        }
    }
}
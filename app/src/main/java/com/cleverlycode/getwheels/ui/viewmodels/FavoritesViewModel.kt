package com.cleverlycode.getwheels.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cleverlycode.getwheels.service.AccountService
import com.cleverlycode.getwheels.service.FavoritesService
import com.cleverlycode.getwheels.service.LogService
import com.cleverlycode.getwheels.domain.models.Car
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val accountService: AccountService,
    private val favoritesService: FavoritesService,
    logService: LogService
) : GetWheelsViewModel(logService) {
    val cars = getFavorites()

    private fun getFavorites(): LiveData<List<Car>> {
        val car = MutableLiveData<List<Car>>()

        viewModelScope.launch() {
            val carIds = favoritesService.getFavoritesIds(accountService.currentUserId)
            car.value = favoritesService.getFavorites(carIds)
            val x = 0
        }

        return car
    }
}
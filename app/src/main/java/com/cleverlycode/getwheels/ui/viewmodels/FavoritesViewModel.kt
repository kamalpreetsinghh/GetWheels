package com.cleverlycode.getwheels.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cleverlycode.getwheels.domain.models.Car
import com.cleverlycode.getwheels.service.FavoritesService
import com.cleverlycode.getwheels.service.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesService: FavoritesService,
    logService: LogService
) : GetWheelsViewModel(logService) {
    fun getFavorites(userId: String): LiveData<List<Car>> {
        val cars = MutableLiveData<List<Car>>()

        viewModelScope.launch {
            val carIds = favoritesService.getFavoritesIds(userId = userId)
            cars.value = favoritesService.getFavorites(carIds)
            val x = 0
        }

        return cars
    }
}
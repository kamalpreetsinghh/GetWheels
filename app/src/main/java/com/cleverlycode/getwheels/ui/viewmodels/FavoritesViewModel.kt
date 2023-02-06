package com.cleverlycode.getwheels.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cleverlycode.getwheels.data.remote.LogService
import com.cleverlycode.getwheels.domain.models.Car
import com.cleverlycode.getwheels.domain.repositories.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
    logService: LogService
) : GetWheelsViewModel(logService) {
    val cars = MutableLiveData(emptyList<Car>())

    init {
        getFavoriteCarsStream()
    }

    private fun getFavoriteCarsStream() {
        viewModelScope.launch {
            favoritesRepository.getFavoriteCarsStream().collectLatest { carsList ->
                cars.value = carsList
            }
        }
    }
}
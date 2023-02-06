package com.cleverlycode.getwheels.ui.viewmodels

import androidx.lifecycle.viewModelScope
import com.cleverlycode.getwheels.data.remote.AccountService
import com.cleverlycode.getwheels.data.remote.LogService
import com.cleverlycode.getwheels.domain.repositories.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarsViewModel @Inject constructor(
    private val accountService: AccountService,
    private val favoritesRepository: FavoritesRepository,
    logService: LogService
) : GetWheelsViewModel(logService) {

    fun favButtonClick(carId: String, isFavorite: Boolean) {
        viewModelScope.launch {
            if (isFavorite) {
                favoritesRepository.addFavorite(
                    carId = carId,
                    userId = accountService.currentUserId
                )
            } else {
                favoritesRepository.removeFavorite(
                    carId = carId,
                    userId = accountService.currentUserId
                )
            }
        }
    }
}
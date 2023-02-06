package com.cleverlycode.getwheels.ui.viewmodels

import androidx.lifecycle.viewModelScope
import com.cleverlycode.getwheels.data.remote.AccountService
import com.cleverlycode.getwheels.data.remote.FavoritesService
import com.cleverlycode.getwheels.data.remote.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarsViewModel @Inject constructor(
    private val accountService: AccountService,
    private val favoritesService: FavoritesService,
    logService: LogService
) : GetWheelsViewModel(logService) {

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
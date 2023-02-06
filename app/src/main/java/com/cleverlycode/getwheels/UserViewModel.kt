package com.cleverlycode.getwheels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cleverlycode.getwheels.data.remote.AccountService
import com.cleverlycode.getwheels.domain.models.Car
import com.cleverlycode.getwheels.domain.repositories.CarsRepository
import com.cleverlycode.getwheels.domain.repositories.FavoritesRepository
import com.cleverlycode.getwheels.utils.InternalStorageUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val carsRepository: CarsRepository,
    private val favoritesRepository: FavoritesRepository,
    private val accountService: AccountService,
) : ViewModel() {
    val isUserLoggedIn = accountService.currentUser != null

    fun syncWithRemoteDataSource(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            if (carsRepository.getCars().isEmpty()) {
                carsRepository.syncWithRemoteDataSource()
                syncImagesInInternalStorage(context = context, cars = carsRepository.getCars())
                syncFavoritesWithRemoteDatabase()
            }
            if (favoritesRepository.getFavoriteCars().isEmpty()) {
                syncFavoritesWithRemoteDatabase()
            }
        }
    }

    private fun syncImagesInInternalStorage(context: Context, cars: List<Car>) {
        viewModelScope.launch {
            InternalStorageUtils.deleteAllImagesFromInternalStorage(
                context = context,
                dirName = "images"
            )
            cars.forEach { car ->
                val carId = car.id
                if (carId != null) {
                    val imageBitmap = carsRepository.getCarImage(carId = carId)
                    if (imageBitmap != null) {
                        InternalStorageUtils.saveImageToInternalStorage(
                            context = context,
                            dirName = "cars",
                            fileName = carId,
                            bitmap = imageBitmap
                        )
                    }
                }
            }
        }
    }

    private fun syncFavoritesWithRemoteDatabase() {
        if (isUserLoggedIn) {
            viewModelScope.launch(Dispatchers.IO) {
                if (favoritesRepository.getFavoriteCars().isEmpty()) {
                    favoritesRepository.syncFavoriteWithRemoteDataSource(userId = accountService.currentUserId)
                }
            }
        }
    }

}
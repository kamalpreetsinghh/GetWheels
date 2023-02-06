package com.cleverlycode.getwheels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cleverlycode.getwheels.data.remote.AccountService
import com.cleverlycode.getwheels.domain.models.Car
import com.cleverlycode.getwheels.domain.repositories.CarsRepository
import com.cleverlycode.getwheels.domain.repositories.FavoritesRepository
import com.cleverlycode.getwheels.utils.InternalStorageUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val carsRepository: CarsRepository,
    private val favoritesRepository: FavoritesRepository,
    private val accountService: AccountService,
) : ViewModel() {
    val isUserLoggedIn = accountService.currentUser != null
    private val _cars = MutableLiveData(emptyList<Car>())
    val cars: LiveData<List<Car>> get() = _cars

    fun getCarsStream(context: Context) {
        viewModelScope.launch {
            carsRepository.getCarsStream().collectLatest { carsList ->
                if (carsList.isEmpty()) {
                    syncWithRemoteDataSource(context = context)
                } else {
                    _cars.value = carsList
                    if (favoritesRepository.getFavoriteCars().isEmpty()) {
                        syncFavoritesWithRemoteDatabase()
                    }
                }
            }
        }
    }

    private fun syncWithRemoteDataSource(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            carsRepository.syncWithRemoteDataSource()
            syncImagesInInternalStorage(context = context, cars = carsRepository.getCars())
            syncFavoritesWithRemoteDatabase()
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
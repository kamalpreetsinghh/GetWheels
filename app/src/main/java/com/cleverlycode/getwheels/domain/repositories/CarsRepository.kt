package com.cleverlycode.getwheels.domain.repositories

import android.graphics.Bitmap
import com.cleverlycode.getwheels.domain.models.Car
import com.cleverlycode.getwheels.domain.models.CarDetail
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.Flow

interface CarsRepository {
    suspend fun getCarsStream(): Flow<List<Car>>
    suspend fun getCars(): List<Car>
    suspend fun getCarDetails(carId: String): CarDetail?
    suspend fun getCarsPictureRefList(carId: String): List<String>
    suspend fun syncWithRemoteDataSource()
    suspend fun getCarImage(carId: String): Bitmap?
}
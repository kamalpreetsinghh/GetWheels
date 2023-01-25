package com.cleverlycode.getwheels.domain.repositories

import com.cleverlycode.getwheels.domain.models.Car
import com.cleverlycode.getwheels.domain.models.CarDetail
import kotlinx.coroutines.flow.Flow

interface CarsRepository {
    suspend fun getCarsStream(): Flow<List<Car>>
    suspend fun getCarDetails(carId: String): CarDetail?
    suspend fun getCarsPictureRefList(carId: String): List<String>
    suspend fun syncWithRemoteDataSource()
    suspend fun syncWithRemoteDataSource(userId: String)
    suspend fun getCarsImages(userId: String)
}
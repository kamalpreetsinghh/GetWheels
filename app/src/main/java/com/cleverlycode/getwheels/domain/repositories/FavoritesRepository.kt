package com.cleverlycode.getwheels.domain.repositories

import com.cleverlycode.getwheels.domain.models.Car
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getFavoriteCarsStream(): Flow<List<Car>>
    suspend fun getFavoriteCars(): List<Car>
    suspend fun syncFavoriteWithRemoteDataSource(userId: String)
    suspend fun addFavorite(carId: String, userId: String)
    suspend fun removeFavorite(carId: String, userId: String)
    suspend fun clearFavorites()
}
package com.cleverlycode.getwheels.service

import com.cleverlycode.getwheels.domain.models.Car

interface FavoritesService {
    suspend fun getFavoritesIds(userId: String): List<String>
    suspend fun getFavorites(carIds: List<String>): List<Car>
    suspend fun addFavorite(carId: String, userId: String)
    suspend fun removeFavorite(carId: String, userId: String)
}
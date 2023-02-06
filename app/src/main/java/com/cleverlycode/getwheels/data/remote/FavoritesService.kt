package com.cleverlycode.getwheels.data.remote

interface FavoritesService {
    suspend fun getFavoritesIds(userId: String): List<String>
    suspend fun addFavorite(carId: String, userId: String)
    suspend fun removeFavorite(carId: String, userId: String)
}
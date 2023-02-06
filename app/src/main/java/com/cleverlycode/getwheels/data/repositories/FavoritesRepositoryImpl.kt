package com.cleverlycode.getwheels.data.repositories

import com.cleverlycode.getwheels.data.local.CarsDatabase
import com.cleverlycode.getwheels.data.mapper.toCar
import com.cleverlycode.getwheels.data.remote.CarsService
import com.cleverlycode.getwheels.data.remote.FavoritesService
import com.cleverlycode.getwheels.domain.models.Car
import com.cleverlycode.getwheels.domain.repositories.FavoritesRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
    private val carsService: CarsService,
    private val favoritesService: FavoritesService,
    carsDatabase: CarsDatabase
) : FavoritesRepository {
    private val dao = carsDatabase.dao

    override suspend fun getFavoriteCarsStream(): Flow<List<Car>> {
        val cars = dao.observeFavCars().transform { carEntities ->
            val carsList: MutableList<Car> = mutableListOf()
            carEntities.forEach { carEntity ->
                runBlocking {
                    val car = carEntity.toCar()
                    val imageRef = async(coroutineContext) {
                        carsService.getCarsPictureRefList(docId = carEntity.id)[0]
                    }
                    imageRef.join()
                    car.imageRef = imageRef.await()
                    carsList.add(car)
                }
            }
            emit(carsList)
        }
        return cars
    }

    override suspend fun getFavoriteCars(): List<Car> =
        dao.getFavoriteCars().map { carEntity ->
            carEntity.toCar()
        }

    override suspend fun syncFavoriteWithRemoteDataSource(userId: String) {
        val carEntities = dao.getCars()
        val favoriteCarsIds = carsService.getFavoritesIds(userId)
        if (favoriteCarsIds != null && favoriteCarsIds.isNotEmpty()) {
            favoriteCarsIds.forEach { carId ->
                carEntities.forEach { carEntity ->
                    if (carEntity.id == carId) {
                        carEntity.isFavorite = true
                        dao.update(carEntity = carEntity)
                    }
                }
            }
        }
    }

    override suspend fun addFavorite(carId: String, userId: String) {
        dao.updateFavorite(id = carId, isFavorite = true)
        favoritesService.addFavorite(carId = carId, userId = userId)
    }

    override suspend fun removeFavorite(carId: String, userId: String) {
        dao.updateFavorite(id = carId, isFavorite = false)
        favoritesService.removeFavorite(carId = carId, userId = userId)
    }

    override suspend fun clearFavorites() {
        val carEntities = dao.getCars()
        carEntities.forEach { carEntity ->
            if (carEntity.isFavorite) {
                carEntity.isFavorite = false
                dao.update(carEntity = carEntity)
            }
        }
    }
}
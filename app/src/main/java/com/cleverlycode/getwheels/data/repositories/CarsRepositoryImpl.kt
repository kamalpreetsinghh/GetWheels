package com.cleverlycode.getwheels.data.repositories

import com.cleverlycode.getwheels.data.local.CarsDatabase
import com.cleverlycode.getwheels.data.mapper.toCar
import com.cleverlycode.getwheels.data.mapper.toCarDetail
import com.cleverlycode.getwheels.data.mapper.toCarEntity
import com.cleverlycode.getwheels.data.remote.CarsService
import com.cleverlycode.getwheels.domain.models.Car
import com.cleverlycode.getwheels.domain.models.CarDetail
import com.cleverlycode.getwheels.domain.repositories.CarsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CarsRepositoryImpl @Inject constructor(
    private val carsService: CarsService,
    db: CarsDatabase
) : CarsRepository {
    private val dao = db.dao

    override suspend fun getCarsStream(): Flow<List<Car>> {
        val cars = dao.observeCars().transform { carEntitiesList ->
            val carsList: MutableList<Car> = mutableListOf()
            carEntitiesList.forEach { carEntity ->
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

    override suspend fun getCarDetails(carId: String): CarDetail? {
        return dao.getCar(carId)?.toCarDetail()
    }

    override suspend fun getCarsPictureRefList(carId: String): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun syncWithRemoteDataSource() {
        withContext(Dispatchers.IO) {
            val cars = carsService.getCars()
            dao.deleteAllCars()
            cars.data?.forEach { car ->
                dao.insert(carEntity = car.toCarEntity())
            }
        }
    }

    override suspend fun syncWithRemoteDataSource(userId: String) {
        withContext(Dispatchers.IO) {
//            val cars = carsService.getCars(userId = userId)
//
//            dao.deleteAllCars()
//            cars.forEach { car ->
//                dao.insert(carEntity = car.toCarEntity())
//            }
        }
    }

    override suspend fun getCarsImages(userId: String) {
        TODO("Not yet implemented")
    }
}
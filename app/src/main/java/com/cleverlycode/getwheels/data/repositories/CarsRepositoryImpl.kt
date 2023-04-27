package com.cleverlycode.getwheels.data.repositories

import android.graphics.Bitmap
import com.cleverlycode.getwheels.data.local.CarsDatabase
import com.cleverlycode.getwheels.data.mapper.toCar
import com.cleverlycode.getwheels.data.mapper.toCarDetail
import com.cleverlycode.getwheels.data.mapper.toCarEntity
import com.cleverlycode.getwheels.data.remote.CarsService
import com.cleverlycode.getwheels.domain.models.Car
import com.cleverlycode.getwheels.domain.models.CarDetail
import com.cleverlycode.getwheels.domain.repositories.CarsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class CarsRepositoryImpl @Inject constructor(
    private val carsService: CarsService,
    db: CarsDatabase
) : CarsRepository {
    private val dao = db.dao

    override fun getCarsStream(): Flow<List<Car>> {
        val cars = dao.observeCars().transform { carEntitiesList ->
            val carsList: MutableList<Car> = mutableListOf()
            carEntitiesList.forEach { carEntity ->
                val car = carEntity.toCar()
                car.imageRef = carsService.getCarsPictureRef(docId = carEntity.id)
                carsList.add(car)
            }
            emit(carsList)
        }
        return cars
    }

    override suspend fun getCars(): List<Car> =
        dao.getCars().map { carEntity ->
            carEntity.toCar()
        }

    override suspend fun getCarDetails(carId: String): CarDetail? =
        dao.getCar(carId)?.toCarDetail()

    override suspend fun getCarsPictureRefList(carId: String): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun syncWithRemoteDataSource() {
        val carsResource = carsService.getCars()
        if (carsResource.data != null) {
            val cars = carsResource.data
            dao.deleteAllCars()
            cars.forEach { car ->
                dao.insert(carEntity = car.toCarEntity())
            }
        }
    }

    override suspend fun getCarImage(carId: String): Bitmap? =
        carsService.getCarPicture(carId = carId)

}
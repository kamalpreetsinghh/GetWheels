package com.cleverlycode.getwheels.service.impl

import com.cleverlycode.getwheels.data.remote.CarsService
import com.cleverlycode.getwheels.domain.models.CarDetail
import com.cleverlycode.getwheels.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CarsServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
) : CarsService {
    override suspend fun getCars(): Resource<List<CarDetail>> {
        val cars: MutableList<CarDetail> = mutableListOf()

        val querySnapshot = firestore
            .collection(CARS_COLLECTION)
            .get()
            .await()

        if (querySnapshot != null && querySnapshot.size() > 0) {
            querySnapshot.forEach { documentSnapshot ->
                val car = documentSnapshot.toObject<CarDetail>()
                car.id = documentSnapshot.id
                car.imageLocation = getCarsPicturesStorageLocation(documentSnapshot.id)
                cars.add(car)
            }
        }

        return Resource.Success(data = cars)
    }

    override suspend fun getCarsImages(userId: String) {
        firestore.collection(CARS_COLLECTION)
            .get()
    }

    override suspend fun getCarsPictureRefList(docId: String): List<StorageReference> =
        storage
            .reference
            .child(getCarsPicturesStorageLocation(docId))
            .listAll()
            .await()
            .items

    private fun getCarsPicturesStorageLocation(uid: String) = "images/cars/$uid/"

    private suspend fun checkIfFavorite(userId: String, carId: String): Boolean {
        var isFavorite = false
        firestore.collection(FAVORITES_COLLECTION)
            .document(userId)
            .get()
            .await()
            .get("carsIds")
            .let {
                val carsIds = it as List<*>
                if (carsIds.contains(carId)) {
                    isFavorite = true
                }
            }
        return isFavorite
    }

    companion object {
        private const val CARS_COLLECTION = "cars"
        private const val FAVORITES_COLLECTION = "favorites"
    }
}


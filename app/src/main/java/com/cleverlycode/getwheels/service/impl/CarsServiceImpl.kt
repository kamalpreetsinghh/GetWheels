package com.cleverlycode.getwheels.service.impl

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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

    override suspend fun getCarsPictureRef(docId: String): StorageReference? {
        val carsPictureReferenceList =  getCarsPictureRefList(docId = docId)
        return if(carsPictureReferenceList.isNotEmpty()) {
            carsPictureReferenceList[0]
        } else null
    }

    override suspend fun getCarsPictureRefList(docId: String): List<StorageReference> =
        storage
            .reference
            .child(getCarsPicturesStorageLocation(docId))
            .listAll()
            .await()
            .items

    private fun getCarsPicturesStorageLocation(carId: String) = "images/cars/$carId/"

    override suspend fun getFavoritesIds(userId: String): List<String> =
        firestore.collection(CarsServiceImpl.FAVORITES_COLLECTION)
            .document(userId)
            .get()
            .await()
            .get("carsIds")
            .let {
                it as List<String>
            }

    override suspend fun getCarPicture(carId: String): Bitmap? {
        val ONE_MEGABYTE: Long = 1024 * 1024
        val storageRef = storage
            .reference
            .child(getCarsPicturesStorageLocation(carId = carId))
            .listAll()
            .await()
            .items[0]

        val bytes = storageRef.getBytes(ONE_MEGABYTE).await()
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    companion object {
        private const val CARS_COLLECTION = "cars"
        private const val FAVORITES_COLLECTION = "favorites"
    }
}


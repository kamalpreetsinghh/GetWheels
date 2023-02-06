package com.cleverlycode.getwheels.service.impl

import com.cleverlycode.getwheels.data.remote.FavoritesService
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FavoritesServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : FavoritesService {
    override suspend fun getFavoritesIds(userId: String): List<String> =
        firestore.collection(FAVORITES_COLLECTION)
            .document(userId)
            .get()
            .await()
            .get("carsIds")
            .let {
                it as List<String>
            }

    override suspend fun addFavorite(carId: String, userId: String) {
        firestore.collection(FAVORITES_COLLECTION).document(userId)
            .update("carsIds", FieldValue.arrayUnion(carId))
    }

    override suspend fun removeFavorite(carId: String, userId: String) {
        firestore.collection(FAVORITES_COLLECTION).document(userId)
            .update("carsIds", FieldValue.arrayRemove(carId))
    }

    private suspend fun getCarsPictureRefs(docId: String): List<StorageReference> =
        storage.reference.child(getCarsPicturesStorageLocation(docId)).listAll().await().items

    private fun getCarsPicturesStorageLocation(uid: String) = "images/cars/$uid/"

    companion object {
        private const val CARS_COLLECTION = "cars"
        private const val FAVORITES_COLLECTION = "favorites"
    }
}
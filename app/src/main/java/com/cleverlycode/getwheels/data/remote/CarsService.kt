package com.cleverlycode.getwheels.data.remote

import android.graphics.Bitmap
import com.cleverlycode.getwheels.domain.models.CarDetail
import com.cleverlycode.getwheels.utils.Resource
import com.google.firebase.storage.StorageReference

interface CarsService {
    suspend fun getCars(): Resource<List<CarDetail>>

    suspend fun getCarsPictureRef(docId: String): StorageReference?
    suspend fun getCarsPictureRefList(docId: String): List<StorageReference>
    suspend fun getFavoritesIds(userId: String): List<String>
    suspend fun getCarPicture(carId: String): Bitmap?
}
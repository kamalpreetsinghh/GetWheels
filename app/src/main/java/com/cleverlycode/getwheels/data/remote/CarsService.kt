package com.cleverlycode.getwheels.data.remote

import com.cleverlycode.getwheels.domain.models.CarDetail
import com.cleverlycode.getwheels.utils.Resource
import com.google.firebase.storage.StorageReference

interface CarsService {
    suspend fun getCars(): Resource<List<CarDetail>>
    suspend fun getCarsImages(userId: String)
    suspend fun getCarsPictureRefList(docId: String): List<StorageReference>
}
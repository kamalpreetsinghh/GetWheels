package com.cleverlycode.getwheels.data.remote

import android.graphics.Bitmap
import com.cleverlycode.getwheels.domain.models.ProfileInfo
import com.cleverlycode.getwheels.utils.Resource
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.Flow

interface ProfileService {
    suspend fun createProfile(profileInfo: ProfileInfo)
    suspend fun getProfileDetails(userId: String): Resource<ProfileInfo>
    suspend fun getProfilePicture(userId: String): Bitmap?
    suspend fun updateProfileDetails(profileInfo: ProfileInfo)
    suspend fun deleteProfile(user: FirebaseUser)
}
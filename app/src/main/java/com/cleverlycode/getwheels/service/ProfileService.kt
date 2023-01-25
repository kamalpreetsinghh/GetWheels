package com.cleverlycode.getwheels.service

import com.cleverlycode.getwheels.domain.models.ProfileInfo
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.Flow

interface ProfileService {
    suspend fun createProfile(profileInfo: ProfileInfo, userId: String)
    suspend fun getProfileDetails(userId: String): Flow<ProfileInfo>
    suspend fun getProfilePictureRef(userId: String): StorageReference
    suspend fun updateProfileDetails(profileInfo: ProfileInfo, userId: String)
    suspend fun deleteProfile(user: FirebaseUser)
}
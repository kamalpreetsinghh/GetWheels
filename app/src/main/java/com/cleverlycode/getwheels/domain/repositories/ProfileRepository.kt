package com.cleverlycode.getwheels.domain.repositories

import android.graphics.Bitmap
import com.cleverlycode.getwheels.domain.models.ProfileInfo

interface ProfileRepository {
    suspend fun getProfileDetails(userId: String): ProfileInfo
    suspend fun createProfile(profileInfo: ProfileInfo)
    suspend fun updateProfileDetails(profileInfo: ProfileInfo)
    suspend fun getProfilePicture(userId: String): Bitmap?
    suspend fun deleteProfileDetails()
}
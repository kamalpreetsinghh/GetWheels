package com.cleverlycode.getwheels.data.repositories

import android.graphics.Bitmap
import com.cleverlycode.getwheels.data.local.CarsDatabase
import com.cleverlycode.getwheels.data.mapper.toProfileEntity
import com.cleverlycode.getwheels.data.mapper.toProfileInfo
import com.cleverlycode.getwheels.data.remote.ProfileService
import com.cleverlycode.getwheels.domain.models.ProfileInfo
import com.cleverlycode.getwheels.domain.repositories.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileService: ProfileService,
    carsDatabase: CarsDatabase
) : ProfileRepository {
    private val dao = carsDatabase.profileDao

    override suspend fun getProfileDetails(userId: String): ProfileInfo {
        val profileInfo: ProfileInfo
        val profileEntity = dao.getProfileDetails(id = userId)
        profileInfo = if (profileEntity != null) {
            profileEntity.toProfileInfo()
        } else {
            val profileInfoResource = profileService.getProfileDetails(userId = userId)
            if (profileInfoResource.data != null) {
                dao.insert(profileEntity = profileInfoResource.data.toProfileEntity())
            }

            profileInfoResource.data ?: ProfileInfo()
        }

        return profileInfo
    }

    override suspend fun createProfile(profileInfo: ProfileInfo) {
        dao.insert(profileEntity = profileInfo.toProfileEntity())
        profileService.createProfile(profileInfo = profileInfo)
    }

    override suspend fun updateProfileDetails(profileInfo: ProfileInfo) {
        dao.update(profileEntity = profileInfo.toProfileEntity())
        profileService.updateProfileDetails(profileInfo = profileInfo)
    }

    override suspend fun getProfilePicture(userId: String): Bitmap? =
        profileService.getProfilePicture(userId = userId)

    override suspend fun deleteProfileDetails() {
        TODO("Not yet implemented")
    }
}
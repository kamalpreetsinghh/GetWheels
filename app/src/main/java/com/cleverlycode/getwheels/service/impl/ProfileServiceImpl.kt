package com.cleverlycode.getwheels.service.impl

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.cleverlycode.getwheels.data.remote.ProfileService
import com.cleverlycode.getwheels.domain.models.ProfileInfo
import com.cleverlycode.getwheels.utils.Resource
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class ProfileServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : ProfileService {
    override suspend fun createProfile(profileInfo: ProfileInfo) {
        val profile = hashMapOf(
            "firstName" to profileInfo.firstName,
            "lastName" to profileInfo.lastName,
            "email" to profileInfo.email
        )

        firestore
            .collection(PROFILE_COLLECTION)
            .document(profileInfo.id)
            .set(profile)
    }

    override suspend fun getProfileDetails(userId: String): Resource<ProfileInfo> {
        val documentSnapshot = firestore
            .collection(PROFILE_COLLECTION)
            .document(userId)
            .get()
            .await()

        var profileInfo = ProfileInfo()
        if (documentSnapshot != null) {
            profileInfo = documentSnapshot.toObject<ProfileInfo>() ?: ProfileInfo()
            if (documentSnapshot.toObject<ProfileInfo>() != null) {
                profileInfo.id = documentSnapshot.id
            }
        }
        return Resource.Success(data = profileInfo)
    }

    override suspend fun updateProfileDetails(profileInfo: ProfileInfo) {
        firestore
            .collection(PROFILE_COLLECTION)
            .document(profileInfo.id)
            .update(
                mapOf(
                    "firstName" to profileInfo.firstName,
                    "lastName" to profileInfo.lastName,
                    "email" to profileInfo.email
                )
            )

        if (profileInfo.imageBitmap != null) {
            updateProfilePicture(profileInfo.imageBitmap, profileInfo.id)
        }
    }

    override suspend fun getProfilePicture(userId: String): Bitmap? {
        val FIVE_MEGABYTE: Long = 5120 * 5120
        val storageRef = storage
            .reference
            .child(getProfileCloudStorageLocation(userId))
        val bytes = storageRef.getBytes(FIVE_MEGABYTE).await()
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    override suspend fun deleteProfile(user: FirebaseUser) {
        deleteFromCloudStorage(user.uid)
        deleteProfileDocument(user.uid)
        deleteUserAccount(user)
    }

    private fun updateProfilePicture(bitmap: Bitmap, userId: String) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val data = byteArrayOutputStream.toByteArray()
        storage
            .reference
            .child(getProfileCloudStorageLocation(userId))
            .putBytes(data)
    }

    private fun deleteFromCloudStorage(userId: String) {
        storage
            .reference
            .child(getProfileCloudStorageLocation(userId = userId))
            .delete()
    }

    private fun deleteProfileDocument(userId: String) {
        firestore
            .collection(PROFILE_COLLECTION)
            .document(userId)
            .delete()
    }

    private fun deleteUserAccount(user: FirebaseUser) {
        user.delete()
    }

    private fun getProfileCloudStorageLocation(userId: String) =
        "images/profiles/$userId/profile.jpg"

    companion object {
        private const val PROFILE_COLLECTION = "profiles"
    }
}
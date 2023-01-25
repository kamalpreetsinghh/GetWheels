package com.cleverlycode.getwheels.service.impl

import android.net.Uri
import com.cleverlycode.getwheels.service.ProfileService
import com.cleverlycode.getwheels.domain.models.ProfileInfo
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ProfileServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : ProfileService {
    override suspend fun createProfile(profileInfo: ProfileInfo, userId: String) {
        firestore
            .collection(PROFILE_COLLECTION)
            .document(userId)
            .set(profileInfo)
    }

    override suspend fun getProfileDetails(userId: String): Flow<ProfileInfo> = callbackFlow {
        val doc = firestore.collection(PROFILE_COLLECTION).document(userId)
        val subscription = doc.addSnapshotListener { snapshot, _ ->
            if (snapshot != null && snapshot.exists()) {
                val profile = snapshot.toObject<ProfileInfo>()
                if (profile != null) trySend(profile).isSuccess
            }
        }

        awaitClose {
            subscription.remove()
        }
    }

    override suspend fun updateProfileDetails(profileInfo: ProfileInfo, userId: String) {
        firestore
            .collection(PROFILE_COLLECTION)
            .document(userId)
            .update(
                mapOf(
                    "firstName" to profileInfo.firstName,
                    "lastName" to profileInfo.lastName,
                    "email" to profileInfo.email
                )
            )

        if (profileInfo.imageUri != null) {
            updateProfilePicture(profileInfo.imageUri, userId)
        }
    }

    override suspend fun getProfilePictureRef(userId: String): StorageReference =
        storage
            .reference
            .child(getProfileCloudStorageLocation(userId))

    override suspend fun deleteProfile(user: FirebaseUser) {
        deleteFromCloudStorage(user.uid)
        deleteProfileDocument(user.uid)
        deleteUserAccount(user)
    }

    private fun updateProfilePicture(uri: Uri, userId: String) {
        storage
            .reference
            .child(getProfileCloudStorageLocation(userId))
            .putFile(uri)
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
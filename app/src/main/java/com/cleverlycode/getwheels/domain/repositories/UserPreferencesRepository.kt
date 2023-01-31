package com.cleverlycode.getwheels.domain.repositories

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val isUserSignedIn: Flow<Boolean>
    suspend fun fetchInitialPreferences(): Boolean
    suspend fun updateIsUserSignedIn(isUserSignedIn: Boolean)
    suspend fun clearDataStore()
}
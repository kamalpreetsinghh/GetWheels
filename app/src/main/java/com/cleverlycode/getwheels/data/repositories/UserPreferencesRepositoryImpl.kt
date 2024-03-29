package com.cleverlycode.getwheels.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.cleverlycode.getwheels.domain.repositories.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesRepository {
    private object PreferencesKeys {
        val IS_USER_SIGNED_IN = booleanPreferencesKey("is_user_signed_in")
    }

    override val isUserSignedIn: Flow<Boolean> = dataStore.data.catch { exception ->
        // dataStore.data throws an IOException when an error is encountered when reading data
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        preferences[PreferencesKeys.IS_USER_SIGNED_IN] ?: false
    }

    override suspend fun fetchInitialPreferences() =
        dataStore.data.first().toPreferences()[PreferencesKeys.IS_USER_SIGNED_IN] ?: false

    override suspend fun updateIsUserSignedIn(isUserSignedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_USER_SIGNED_IN] = isUserSignedIn
        }
    }

    override suspend fun clearDataStore() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
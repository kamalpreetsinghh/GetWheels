package com.cleverlycode.getwheels.ui.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.cleverlycode.getwheels.data.remote.AccountService
import com.cleverlycode.getwheels.data.remote.LogService
import com.cleverlycode.getwheels.domain.models.ProfileInfo
import com.cleverlycode.getwheels.domain.repositories.ProfileRepository
import com.cleverlycode.getwheels.domain.repositories.UserPreferencesRepository
import com.cleverlycode.getwheels.ui.models.Profile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val accountService: AccountService,
    private val profileRepository: ProfileRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    logService: LogService
) : GetWheelsViewModel(logService) {
    val profileUiState = MutableLiveData<Profile>()
    val isUserSignedIn = accountService.currentUser != null

    init {
        getProfileInfo()
    }

    private fun getProfileInfo() {
        viewModelScope.launch {
            if (accountService.currentUser != null) {
                val profileInfo: ProfileInfo =
                    profileRepository.getProfileDetails(accountService.currentUserId)
                profileUiState.value = Profile(
                    firstName = profileInfo.firstName,
                    lastName = profileInfo.lastName,
                    email = profileInfo.email
                )
            }
        }
    }

    fun getProfilePictureAsync(): Deferred<Bitmap?> =
        viewModelScope.async {
            runBlocking {
                profileRepository.getProfilePicture(accountService.currentUserId)
            }
        }

    fun logout(action: NavDirections, navigate: (NavDirections) -> Unit) {
        viewModelScope.launch {
            accountService.signOut()
            userPreferencesRepository.updateIsUserSignedIn(isUserSignedIn = false)
            navigate(action)
        }
    }

    fun navigateToEditProfile(action: NavDirections, navigate: (NavDirections) -> Unit) {
        navigate(action)
    }
}
package com.cleverlycode.getwheels.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.cleverlycode.getwheels.data.repositories.UserPreferencesRepository
import com.cleverlycode.getwheels.service.AccountService
import com.cleverlycode.getwheels.service.LogService
import com.cleverlycode.getwheels.service.ProfileService
import com.cleverlycode.getwheels.ui.models.Profile
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val accountService: AccountService,
    private val profileService: ProfileService,
    private val userPreferencesRepository: UserPreferencesRepository,
    logService: LogService
) : GetWheelsViewModel(logService) {
    val profileUiState: LiveData<Profile> = getProfileInfo()

    private fun getProfileInfo(): LiveData<Profile> = liveData(showErrorExceptionHandler) {
        profileService.getProfileDetails(accountService.currentUserId).collect {
            emit(
                Profile(
                    firstName = it.firstName,
                    lastName = it.lastName,
                    email = it.email
                )
            )
        }
    }

    fun getProfilePictureReference(): LiveData<StorageReference> {
        val storageReference = MutableLiveData<StorageReference>()
        viewModelScope.launch {
            storageReference.value =
                profileService.getProfilePictureRef(accountService.currentUserId)
        }

        return storageReference
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

    fun navigateToProfileDetails(action: NavDirections, navigate: (NavDirections) -> Unit) {
        navigate(action)
    }
}
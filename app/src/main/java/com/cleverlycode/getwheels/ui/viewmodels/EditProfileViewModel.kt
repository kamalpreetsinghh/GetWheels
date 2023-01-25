package com.cleverlycode.getwheels.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.cleverlycode.getwheels.service.AccountService
import com.cleverlycode.getwheels.service.LogService
import com.cleverlycode.getwheels.service.ProfileService
import com.cleverlycode.getwheels.domain.models.ProfileInfo
import com.cleverlycode.getwheels.ui.models.Profile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val accountService: AccountService,
    private val profileService: ProfileService,
    logService: LogService
) : GetWheelsViewModel(logService) {
    private val _profileUiState by lazy {
        MutableLiveData(Profile())
    }

    val profileUiState: LiveData<Profile> by lazy {
        _profileUiState
    }

    private val firstName get() = profileUiState.value?.firstName ?: ""
    private val lastName get() = profileUiState.value?.lastName ?: ""
    private val email get() = profileUiState.value?.email ?: ""

    fun initializeEditProfileValues(profile: Profile) {
        _profileUiState.value = profile
    }

    fun updateProfilePicture(uri: Uri) {
        _profileUiState.value = profileUiState.value?.copy(imageUri = uri)
    }

    fun onFirstNameChange(newValue: CharSequence, start: Int, before: Int, count: Int) {
        _profileUiState.value = profileUiState.value?.copy(firstName = newValue.toString())
        enableOrDisableButton()
    }

    fun onLastNameChange(newValue: CharSequence, start: Int, before: Int, count: Int) {
        _profileUiState.value = profileUiState.value?.copy(lastName = newValue.toString())
        enableOrDisableButton()
    }

    fun onEmailChange(newValue: CharSequence, start: Int, before: Int, count: Int) {
        _profileUiState.value = profileUiState.value?.copy(email = newValue.toString())
        enableOrDisableButton()
    }

    fun onUpdateProfileButtonClick(action: NavDirections, navigate: (NavDirections) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            profileService.updateProfileDetails(
                profileInfo = ProfileInfo(
                    firstName = profileUiState.value?.firstName ?: "",
                    lastName = profileUiState.value?.lastName ?: "",
                    email = profileUiState.value?.email ?: "",
                    imageUri = profileUiState.value?.imageUri
                ),
                userId = accountService.currentUserId
            )
        }
        navigate(action)
    }

    private fun enableOrDisableButton() {
        _profileUiState.value =
            profileUiState.value?.copy(isButtonEnabled = isButtonEnabled())
    }

    private fun isButtonEnabled() = firstName.isNotEmpty() && lastName.isNotEmpty()
            && email.isNotEmpty()
}
package com.cleverlycode.getwheels.ui.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.cleverlycode.getwheels.data.remote.AccountService
import com.cleverlycode.getwheels.data.remote.LogService
import com.cleverlycode.getwheels.data.remote.ProfileService
import com.cleverlycode.getwheels.domain.models.ProfileInfo
import com.cleverlycode.getwheels.domain.repositories.ProfileRepository
import com.cleverlycode.getwheels.ui.models.Profile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val accountService: AccountService,
    private val profileService: ProfileService,
    private val profileRepository: ProfileRepository,
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

    fun updateImageBitmap(bitmap: Bitmap) {
        _profileUiState.value = _profileUiState.value?.copy(imageBitmap = bitmap)
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
        viewModelScope.launch {
            profileRepository.updateProfileDetails(
                profileInfo = ProfileInfo(
                    id = accountService.currentUserId,
                    firstName = profileUiState.value?.firstName ?: "",
                    lastName = profileUiState.value?.lastName ?: "",
                    email = profileUiState.value?.email ?: "",
                    imageBitmap = profileUiState.value?.imageBitmap
                )
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
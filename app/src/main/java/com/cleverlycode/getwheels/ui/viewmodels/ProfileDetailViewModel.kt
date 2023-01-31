package com.cleverlycode.getwheels.ui.viewmodels

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.cleverlycode.getwheels.domain.repositories.UserPreferencesRepository
import com.cleverlycode.getwheels.service.AccountService
import com.cleverlycode.getwheels.service.LogService
import com.cleverlycode.getwheels.service.ProfileService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    private val profileService: ProfileService,
    private val accountService: AccountService,
    private val userPreferencesRepository: UserPreferencesRepository,
    logService: LogService
) : GetWheelsViewModel(logService) {
    fun deleteAccount() {
        viewModelScope.launch {
            accountService.currentUser?.let { profileService.deleteProfile(it) }
        }
    }

    fun logout(action: NavDirections, navigate: (NavDirections) -> Unit) {
        viewModelScope.launch {
            userPreferencesRepository.updateIsUserSignedIn(isUserSignedIn = false)
            navigate(action)
        }
    }
}
package com.cleverlycode.getwheels.ui.viewmodels

import androidx.navigation.NavDirections
import com.cleverlycode.getwheels.data.remote.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    logService: LogService
) : GetWheelsViewModel(logService) {
    fun signInButtonClick(action: NavDirections, navigate: (NavDirections) -> Unit) {
        navigate(action)
    }

    fun signUpButtonClick(action: NavDirections, navigate: (NavDirections) -> Unit) {
        navigate(action)
    }
}
package com.cleverlycode.getwheels.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.cleverlycode.getwheels.R
import com.cleverlycode.getwheels.domain.repositories.UserPreferencesRepository
import com.cleverlycode.getwheels.data.remote.AccountService
import com.cleverlycode.getwheels.data.remote.LogService
import com.cleverlycode.getwheels.ui.models.Login
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountService: AccountService,
    private val userPreferencesRepository: UserPreferencesRepository,
    logService: LogService,
) : GetWheelsViewModel(logService) {
    private val _loginUiState by lazy {
        MutableLiveData(Login())
    }

    val loginUiState: LiveData<Login> by lazy {
        _loginUiState
    }

    private val email get() = loginUiState.value?.email ?: ""
    private val password get() = loginUiState.value?.password ?: ""

    val isUserSignedIn = userPreferencesRepository.isUserSignedIn.asLiveData()

    fun onEmailChange(newValue: CharSequence, start: Int, before: Int, count: Int) {
        _loginUiState.value = _loginUiState.value?.copy(email = newValue.toString())
        enableOrDisableButton()
        _loginUiState.value = _loginUiState.value?.copy(
            isEmailError = false,
            emailErrorMsgResId = null
        )
    }

    fun onPasswordChange(newValue: CharSequence, start: Int, before: Int, count: Int) {
        _loginUiState.value = _loginUiState.value?.copy(password = newValue.toString())
        enableOrDisableButton()
        _loginUiState.value = _loginUiState.value?.copy(
            isPasswordError = false,
            passwordErrorMsgResId = null
        )
    }

    fun onSignInButtonClick(action: NavDirections, navigate: (NavDirections) -> Unit) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
            onError(exception)
            showIncorrectLoginDetailsMsg()
        }
        viewModelScope.launch(coroutineExceptionHandler) {
            accountService.signInWithEmailAndPassword(email, password)
            viewModelScope.launch(Dispatchers.IO) {
                userPreferencesRepository.updateIsUserSignedIn(isUserSignedIn = true)
            }
            navigate(action)
        }
    }

    fun onForgotPasswordClick(action: NavDirections, navigate: (NavDirections) -> Unit) {
        navigate(action)
    }

    fun onSignUpButtonClick(action: NavDirections, navigate: (NavDirections) -> Unit) {
        navigate(action)
    }

    private fun enableOrDisableButton() {
        _loginUiState.value =
            _loginUiState.value?.copy(isButtonEnabled = isButtonEnabled())
    }

    private fun isButtonEnabled() = email.isNotEmpty() && password.isNotEmpty()

    private fun showIncorrectLoginDetailsMsg() {
        _loginUiState.value = _loginUiState.value?.copy(
            isEmailError = true,
            isPasswordError = true,
            passwordErrorMsgResId = R.string.error_incorrect_credentials
        )
    }
}
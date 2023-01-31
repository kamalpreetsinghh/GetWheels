package com.cleverlycode.getwheels.ui.viewmodels

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.cleverlycode.getwheels.R
import com.cleverlycode.getwheels.domain.models.ProfileInfo
import com.cleverlycode.getwheels.service.AccountService
import com.cleverlycode.getwheels.service.LogService
import com.cleverlycode.getwheels.service.ProfileService
import com.cleverlycode.getwheels.ui.models.SignUp
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val accountService: AccountService,
    private val profileService: ProfileService,
    logService: LogService
) : GetWheelsViewModel(logService) {
    private val _signUpUiState by lazy {
        MutableLiveData(SignUp())
    }

    val signUpUiState: LiveData<SignUp> by lazy {
        _signUpUiState
    }

    private val firstName get() = signUpUiState.value?.firstName ?: ""
    private val lastName get() = signUpUiState.value?.lastName ?: ""
    private val email get() = signUpUiState.value?.email ?: ""
    private val password get() = signUpUiState.value?.password ?: ""
    private val confirmPassword get() = signUpUiState.value?.confirmPassword ?: ""

    fun onFirstNameChange(newValue: CharSequence, start: Int, before: Int, count: Int) {
        _signUpUiState.value = signUpUiState.value?.copy(firstName = newValue.toString())
        enableOrDisableButton()
    }

    fun onLastNameChange(newValue: CharSequence, start: Int, before: Int, count: Int) {
        _signUpUiState.value = signUpUiState.value?.copy(lastName = newValue.toString())
        enableOrDisableButton()
    }

    fun onEmailChange(newValue: CharSequence, start: Int, before: Int, count: Int) {
        _signUpUiState.value = signUpUiState.value?.copy(
            email = newValue.toString(),
            isEmailError = false,
            emailErrorMsgResId = null
        )
        enableOrDisableButton()
    }

    fun onPasswordChange(newValue: CharSequence, start: Int, before: Int, count: Int) {
        _signUpUiState.value = signUpUiState.value?.copy(
            password = newValue.toString(),
            isPasswordError = false,
            passwordErrorMsgResId = null
        )
        enableOrDisableButton()
    }

    fun onConfirmPasswordChange(newValue: CharSequence, start: Int, before: Int, count: Int) {
        _signUpUiState.value = signUpUiState.value?.copy(
            confirmPassword = newValue.toString(),
            isConfirmPasswordError = false,
            confirmPasswordErrorMsgResId = null
        )
        enableOrDisableButton()
    }

    fun onSignUpClick(action: NavDirections, navigate: (NavDirections) -> Unit) {
        if (isValidSignUpDetails()) {
            val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
                onError(exception)
                if (exception is FirebaseAuthUserCollisionException) emailAlreadyExists()
                else if (exception is FirebaseAuthWeakPasswordException) weakPassword()
            }
            viewModelScope.launch(coroutineExceptionHandler) {
                val uid = accountService.createAccount(email, password)
                if (uid != null) {
                    val profileInfo = ProfileInfo(
                        firstName = firstName,
                        lastName = lastName,
                        email = email
                    )
                    profileService.createProfile(profileInfo, uid)
                }
                navigate(action)
            }
        }
    }

    fun onSignInClick(action: NavDirections, navigate: (NavDirections) -> Unit) {
        navigate(action)
    }

    private fun isValidSignUpDetails(): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _signUpUiState.value = signUpUiState.value?.copy(
                isEmailError = true,
                emailErrorMsgResId = R.string.error_invalid_email
            )
            return false
        }

        if (!doPasswordsMatch()) {
            _signUpUiState.value = signUpUiState.value?.copy(
                isConfirmPasswordError = true,
                confirmPasswordErrorMsgResId = R.string.error_confirm_password
            )
            return false
        }

        return true
    }

    private fun doPasswordsMatch() = password == confirmPassword

    private fun emailAlreadyExists() {
        _signUpUiState.value = signUpUiState.value?.copy(
            isEmailError = true,
            emailErrorMsgResId = R.string.error_email_already_exists
        )
    }

    private fun weakPassword() {
        _signUpUiState.value = signUpUiState.value?.copy(
            isPasswordError = true,
            isConfirmPasswordError = true,
            confirmPasswordErrorMsgResId = R.string.error_weak_password
        )
    }

    private fun enableOrDisableButton() {
        _signUpUiState.value =
            signUpUiState.value?.copy(isButtonEnabled = isButtonEnabled())
    }

    private fun isButtonEnabled() = firstName.isNotEmpty() && lastName.isNotEmpty()
            && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()
}
package com.cleverlycode.getwheels.ui.models

import androidx.annotation.StringRes

data class Login(
    val email: String = "",
    val isEmailError: Boolean = false,
    @StringRes val emailErrorMsgResId: Int? = null,
    val password: String = "",
    val isPasswordError: Boolean = false,
    @StringRes val passwordErrorMsgResId: Int? = null,
    val isButtonEnabled: Boolean = email.isNotEmpty() && password.isNotEmpty()
)

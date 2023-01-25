package com.cleverlycode.getwheels.ui.models

import android.net.Uri

data class Profile(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val imageUri: Uri? = null,
    val isButtonEnabled: Boolean = true
)
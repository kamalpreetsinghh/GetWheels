package com.cleverlycode.getwheels.ui.models

import android.graphics.Bitmap

data class Profile(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val imageBitmap: Bitmap? = null,
    val isButtonEnabled: Boolean = true
)
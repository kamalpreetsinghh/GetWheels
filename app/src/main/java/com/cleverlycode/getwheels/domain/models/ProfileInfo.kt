package com.cleverlycode.getwheels.domain.models

import android.net.Uri

data class ProfileInfo(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val imageUri: Uri? = null
)
package com.cleverlycode.getwheels.domain.models

import android.graphics.Bitmap
import android.net.Uri

data class ProfileInfo(
    var id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val imageBitmap: Bitmap? = null
)
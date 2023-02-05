package com.cleverlycode.getwheels.data.mapper

import com.cleverlycode.getwheels.data.local.entity.ProfileEntity
import com.cleverlycode.getwheels.domain.models.ProfileInfo

fun ProfileEntity.toProfileInfo(): ProfileInfo =
    ProfileInfo(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email
    )

fun ProfileInfo.toProfileEntity(): ProfileEntity =
    ProfileEntity(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email
    )

package com.cleverlycode.getwheels.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
data class ProfileEntity(
    @PrimaryKey()
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = ""
)
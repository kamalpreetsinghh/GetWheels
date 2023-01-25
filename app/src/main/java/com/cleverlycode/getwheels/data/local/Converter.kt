package com.cleverlycode.getwheels.data.local

import androidx.room.TypeConverter
import com.google.firebase.storage.StorageReference
import java.lang.ProcessBuilder.Redirect.to
import java.util.*

class Converters {
    @TypeConverter
    fun storageRefToString(storageRef: StorageReference?): String? = storageRef?.toString()
}
package com.cleverlycode.getwheels.data.local

import androidx.room.TypeConverter
import com.google.firebase.storage.StorageReference

class Converters {
    @TypeConverter
    fun storageRefToString(storageRef: StorageReference?): String? = storageRef?.toString()
}
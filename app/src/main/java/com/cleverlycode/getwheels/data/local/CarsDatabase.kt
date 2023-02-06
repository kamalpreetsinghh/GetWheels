package com.cleverlycode.getwheels.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cleverlycode.getwheels.data.local.dao.CarDao
import com.cleverlycode.getwheels.data.local.dao.ProfileDao
import com.cleverlycode.getwheels.data.local.entity.CarEntity
import com.cleverlycode.getwheels.data.local.entity.ProfileEntity

@Database(
    entities = [CarEntity::class, ProfileEntity::class],
    version = 6,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class CarsDatabase : RoomDatabase() {
    abstract val dao: CarDao
    abstract val profileDao: ProfileDao
}
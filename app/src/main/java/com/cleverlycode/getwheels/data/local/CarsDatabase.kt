package com.cleverlycode.getwheels.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [CarEntity::class],
    version = 3,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class CarsDatabase : RoomDatabase() {
    abstract val dao: CarDao
}
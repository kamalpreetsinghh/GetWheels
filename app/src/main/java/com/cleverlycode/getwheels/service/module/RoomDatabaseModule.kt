package com.cleverlycode.getwheels.service.module

import android.app.Application
import androidx.room.Room
import com.cleverlycode.getwheels.data.local.CarsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDatabaseModule {
    @Provides
    @Singleton
    fun provideNotesDatabase(app: Application): CarsDatabase =
        Room.databaseBuilder(app, CarsDatabase::class.java, "cars_database")
            .fallbackToDestructiveMigration()
            .build()
}
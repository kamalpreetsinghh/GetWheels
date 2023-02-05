package com.cleverlycode.getwheels.service.module

import com.cleverlycode.getwheels.data.repositories.CarsRepositoryImpl
import com.cleverlycode.getwheels.data.repositories.ProfileRepositoryImpl
import com.cleverlycode.getwheels.data.repositories.UserPreferencesRepositoryImpl
import com.cleverlycode.getwheels.domain.repositories.CarsRepository
import com.cleverlycode.getwheels.domain.repositories.ProfileRepository
import com.cleverlycode.getwheels.domain.repositories.UserPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindsCarsRepository(impl: CarsRepositoryImpl): CarsRepository

    @Binds
    @Singleton
    abstract fun bindsProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindsUserPreferencesRepository(impl: UserPreferencesRepositoryImpl): UserPreferencesRepository

}
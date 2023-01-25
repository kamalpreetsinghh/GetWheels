package com.cleverlycode.getwheels.service.module

import com.cleverlycode.getwheels.data.repositories.CarsRepositoryImpl
import com.cleverlycode.getwheels.domain.repositories.CarsRepository
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

}
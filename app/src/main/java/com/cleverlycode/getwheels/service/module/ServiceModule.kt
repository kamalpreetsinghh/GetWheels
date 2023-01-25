package com.cleverlycode.getwheels.service.module

import com.cleverlycode.getwheels.data.remote.CarsService
import com.cleverlycode.getwheels.service.*
import com.cleverlycode.getwheels.service.impl.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    abstract fun provideAccountService(impl: AccountServiceImpl): AccountService

    @Binds
    abstract fun provideProfileService(impl: ProfileServiceImpl): ProfileService

    @Binds
    abstract fun provideCarService(impl: CarsServiceImpl): CarsService

    @Binds
    abstract fun provideBookingService(impl: BookingServiceImpl): BookingService

    @Binds
    abstract fun provideFavoritesService(impl: FavoritesServiceImpl): FavoritesService

    @Binds
    abstract fun providesLogService(impl: LogServiceImpl): LogService
}
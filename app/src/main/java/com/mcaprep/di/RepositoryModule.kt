package com.mcaprep.di

import com.mcaprep.data.repository.AuthRepository
import com.mcaprep.data.repository.TestSeriesRepository
import com.mcaprep.data.repository.impl.AuthRepositoryImpl
import com.mcaprep.data.repository.impl.TestSeriesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    abstract fun bindTestSeriesRepository(
        impl: TestSeriesRepositoryImpl
    ): TestSeriesRepository


}
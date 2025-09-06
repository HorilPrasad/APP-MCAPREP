package com.mcaprep.di

import com.mcaprep.data.local.AppDatabase
import com.mcaprep.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao = database.userDao()
}
package com.mcaprep.di

import android.content.Context
import androidx.room.Room
import com.mcaprep.data.local.AppDatabase
import com.mcaprep.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, Constants.APP_DATABASE).build()

//    @Provides
//    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()
}

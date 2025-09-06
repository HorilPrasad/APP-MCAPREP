package com.mcaprep.di

import android.content.Context
import android.content.SharedPreferences
import com.mcaprep.data.local.PrefManager
import com.mcaprep.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(Constants.PREFS_MCAPREP, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideSharedPreferencesEditor(prefs: SharedPreferences): PrefManager {
        return PrefManager(prefs)
    }
}


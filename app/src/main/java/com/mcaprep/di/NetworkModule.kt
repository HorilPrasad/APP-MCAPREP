package com.mcaprep.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mcaprep.data.local.PrefManager
import com.mcaprep.data.remote.api.ApiService
import com.mcaprep.data.remote.interceptor.UnauthorizedInterceptor
import com.mcaprep.data.remote.interceptor.AuthInterceptor
import com.mcaprep.utils.Constants
import com.mcaprep.utils.Environment
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideAuthInterceptor(pref: PrefManager): AuthInterceptor = AuthInterceptor(pref)

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideUnauthorizedInterceptor(): UnauthorizedInterceptor = UnauthorizedInterceptor()

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        prettyPrint = false
        isLenient = true
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        loggingInterceptor: HttpLoggingInterceptor,
        unauthorizedInterceptor: UnauthorizedInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(unauthorizedInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(getBaseUrl(Constants.APP_ENV))
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun getBaseUrl(env: Environment): String {
        return when (env) {
            Environment.DEV -> Constants.BASE_URL_DEV
            Environment.STAGE -> Constants.BASE_URL_DEV
            Environment.PROD -> Constants.BASE_URL_PROD
        }
    }
}

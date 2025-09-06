package com.mcaprep.data.remote.interceptor

import com.mcaprep.data.local.PrefManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val pref: PrefManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token: String = pref.getAuthToken() ?: ""
        val request = chain.request().newBuilder()
            .addHeader("Authorization", token)
            .build()
        return chain.proceed(request)
    }
}

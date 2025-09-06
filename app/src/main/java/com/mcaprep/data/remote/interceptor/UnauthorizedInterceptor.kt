package com.mcaprep.data.remote.interceptor

import com.mcaprep.MyApplication
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class UnauthorizedInterceptor @Inject constructor(
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (response.code == 440) {
            MyApplication.logout()
        }

        return response
    }
}

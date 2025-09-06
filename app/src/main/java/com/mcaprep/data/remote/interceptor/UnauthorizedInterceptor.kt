package com.mcaprep.data.remote.interceptor

import com.mcaprep.ui.viewmodel.AuthViewModel
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class UnauthorizedInterceptor @Inject constructor(
    private val authViewModel: AuthViewModel
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (response.code == 401) {
            authViewModel.logout(activity)
        }

        return response
    }
}

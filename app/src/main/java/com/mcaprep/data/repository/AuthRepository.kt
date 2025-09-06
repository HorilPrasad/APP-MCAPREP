package com.mcaprep.data.repository

import com.mcaprep.data.remote.model.LoginRequest
import com.mcaprep.data.remote.model.LoginResponse
import com.mcaprep.data.remote.model.NotificationImageResponse

interface AuthRepository {
    suspend fun login(loginRequest: LoginRequest): LoginResponse
    suspend fun notificationImage(): NotificationImageResponse
}
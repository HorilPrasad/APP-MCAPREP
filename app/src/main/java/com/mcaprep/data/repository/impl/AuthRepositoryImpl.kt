package com.mcaprep.data.repository.impl

import com.mcaprep.data.local.dao.UserDao
import com.mcaprep.data.remote.api.ApiService
import com.mcaprep.data.remote.model.LoginRequest
import com.mcaprep.data.remote.model.LoginResponse
import com.mcaprep.data.remote.model.NotificationImageResponse
import com.mcaprep.data.repository.AuthRepository
import com.mcaprep.domain.mapper.toEntity
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao
): AuthRepository {
    override suspend fun login(loginRequest: LoginRequest): LoginResponse {
        try {
            val response = apiService.login(loginRequest)
            if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse != null) {
                    val user = loginResponse.user.toEntity()
//                    val project = loginResponse.user.project.toEntity(user.id)
                    userDao.insertUser(user)
                    return loginResponse
                }
                throw Exception("Response body is null")
            } else {
                throw Exception("Login failed: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun notificationImage(): NotificationImageResponse {
        try {
            val response = apiService.notificationImage()
            if (response.isSuccessful) {
                return response.body() ?: throw Exception("Response body is null")
            } else {
                throw Exception("Login failed: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }
}
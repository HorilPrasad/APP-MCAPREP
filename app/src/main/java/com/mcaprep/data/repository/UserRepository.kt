package com.mcaprep.data.repository

import com.mcaprep.data.local.entities.UserEntity

interface UserRepository {
    suspend fun getUser(id: String): UserEntity
}

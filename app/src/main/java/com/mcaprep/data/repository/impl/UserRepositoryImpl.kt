package com.mcaprep.data.repository.impl

import com.mcaprep.data.local.dao.UserDao
import com.mcaprep.data.local.entities.UserEntity
import com.mcaprep.data.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {
    override suspend fun getUser(id: String): UserEntity {
        val user = userDao.getUserById(id)
        if (user != null)
            return user
        else
            throw Exception("User not found")
    }
}
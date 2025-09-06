package com.mcaprep.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.*

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String, // _id from JSON
    val email: String,
    val login: String,
    val role: String,
    val name: String,
    val createdAt: String,
    val updatedAt: String,
    val lastDate: String,
    val streak: Int,
    val college: String,
    val date: String,
    val number: String,
    val plan: String,
    val startDate: String,
    val duration: Long,

    @Embedded(prefix = "details_")
    val userDetails: UserDetailsEntity
)

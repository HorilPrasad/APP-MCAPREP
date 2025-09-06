package com.mcaprep.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("Token")
    val token: String,
    @SerialName("User")
    val user: UserDto
)

@Serializable
data class UserDto(
    @SerialName("_id")
    val id: String,
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
    val project: List<ProjectDto>,
    val userDetails: UserPersonalDetailsDto
)

@Serializable
data class ProjectDto(
    val projectId: String,
    val paymentId: String,
    val purchaseDate: String,
    @SerialName("_id")
    val id: String
)

@Serializable
data class UserPersonalDetailsDto(
    val phoneNumber: String,
    val dob: String,
    val college: String,
    val examYear: String
)

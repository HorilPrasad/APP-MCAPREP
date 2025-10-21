package com.mcaprep.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val role: String,
    val loginMethod: String,
    val college: String,
    val plan: String,
    val startDate: String,
    val duration: Long,
    val streak: Int,
    val lastActiveDate: String,
    val examYear: String,
    val dob: String,
    val projects: List<UserProject>
)

data class RankedUser(
    val rank: Int,
    val name: String,
    val marks: Float,
    val isCurrentUser: Boolean = false
)

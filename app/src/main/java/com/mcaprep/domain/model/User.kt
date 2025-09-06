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


package com.mcaprep.domain.model

data class TestItem(
    val id: String,
    val name: String,
    val schedule: String,  // You might want to convert this to a Date object depending on use case
    val duration: Int,
    val difficulty: String,
    val premium: Boolean,
    val weightage: String,
    val createdAt: String,  // Again, convert to Date if needed
    val updatedAt: String,  // Convert to Date if needed
    val isSectionWise: Boolean,
    val questionLength: Int,
    val attemptedLength: Int,
    val totalAttempt: Int,
    val totalScore: Int
)
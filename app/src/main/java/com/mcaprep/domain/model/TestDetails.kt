package com.mcaprep.domain.model

data class TestDetails(
    val id: String,
    val name: String,
    val schedule: String,
    val duration: Int,
    val totalScore: Int,
    val totalAttempt: Int,
    val difficulty: String,
    val startTime: String,
    val remainingSecond: Int,
    val isSectionWise: Boolean,
    val questions: List<Question>
)

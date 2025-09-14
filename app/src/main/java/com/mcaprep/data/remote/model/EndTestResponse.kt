package com.mcaprep.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EndTestResponse(
    @SerialName("Success")
    val success: SuccessDto
)

@Serializable
data class SuccessDto(
    val result: ResultDto,
    val questions: List<QuestionDto>,
    val streak: Int
)

@Serializable
data class ResultDto(
    val testId: String,
    val solution: List<SolutionsDto>,
    val score: String,
    val count: Int,
    val isSectionWise: Boolean,
    @SerialName("total_score")
    val totalScore: Int,
    val attempted: String
)
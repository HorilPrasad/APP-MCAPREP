package com.mcaprep.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TestHistoryResponse(
    @SerialName("Success") val success: TestHistoryDto
)

@Serializable
data class TestHistoryDto(
    @SerialName("detail")
    val detail: DetailDto,
    @SerialName("leaderBoard")
    val leaderBoard: List<LeaderBoardItemDto>
)

@Serializable
data class DetailDto(
    @SerialName("_id") val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("schedule")
    val schedule: String,
    @SerialName("duration")
    val duration: Int,
    @SerialName("difficulty")
    val difficulty: String,
    @SerialName("questions")
    val questions: List<QuestionDto>,
    @SerialName("premium")
    val premium: Boolean,
    @SerialName("weightage")
    val weightage: WeightageDto,
    @SerialName("flag")
    val flag: Boolean,
    @SerialName("isSectionWise")
    val isSectionWise: Boolean,
    val createdAt: String,
    val updatedAt: String,
    @SerialName("total_score")
    val totalScore: Int,
    @SerialName("attempted")
    val attempted: String,
    @SerialName("score")
    val score: Int,
    @SerialName("count")
    val count: Int,
    @SerialName("__v") val version: Int
)

@Serializable
data class WeightageDto(
    @SerialName("_id") val id: String,
    val name: String,
    @SerialName("Subject") val subject: List<SubjectItemDto>,
    val createdAt: String,
    val updatedAt: String,
    @SerialName("__v") val version: Int
)

@Serializable
data class SubjectItemDto(
    val name: String,
    val weight: Double,
    val negative: Double,
    @SerialName("_id") val id: String
)

@Serializable
data class LeaderBoardItemDto(
    val key: String,
    val value: LeaderBoardValueDto
)

@Serializable
data class LeaderBoardValueDto(
    val name: String,
    val marks: Float
)

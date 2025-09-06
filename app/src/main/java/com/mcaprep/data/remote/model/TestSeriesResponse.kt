package com.mcaprep.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TestSeriesResponse(
    @SerialName("Success")
    val res: TestSeriesModelDto,

    )

@Serializable
data class TestSeriesModelDto(
    @SerialName("newData")
    val data: List<TestItemDto>,
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String,
    @SerialName("price")
    val price: Int,
    @SerialName("type")
    val type: String
)

@Serializable
data class TestItemDto(
    @SerialName("_id") val id: String,
    val name: String,
    val schedule: String,  // You might want to convert this to a Date object depending on use case
    val duration: Int,
    val difficulty: String,
    val premium: Boolean,
    val weightage: String,
    val createdAt: String,  // Again, convert to Date if needed
    val updatedAt: String,  // Convert to Date if needed
    @SerialName("__v") val version: Int,
    val isSectionWise: Boolean,
    val questionLength: Int,
    val attemptedLength: Int,
    val totalAttempt: Int,
    @SerialName("total_score")
    val totalScore: Int
)
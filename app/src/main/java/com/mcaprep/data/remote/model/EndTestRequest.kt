package com.mcaprep.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EndTestRequest(
    @SerialName("testId")
    val testId: String,
    @SerialName("solution")
    val solution: List<SolutionsDto>
)

@Serializable
data class SolutionsDto(
    @SerialName("question")
    val question: String,
    @SerialName("option")
    val option: String?
)
package com.mcaprep.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StartExistingTestResponse(
    @SerialName("Success")
    val res: TestDetailsDto,

    )
@Serializable
class TestDetailsDto(
    @SerialName("_id")
    val id: String? = null,
    val name: String? = null,
    val schedule: String? = null,
    val duration: Int? = null,
    @SerialName("total_score")
    val totalScore: Int? = null,
    val totalAttempt: Int? = null,
    val difficulty: String? = null,
    @SerialName("start_time")
    val startTime: String? = null,
    @SerialName("remaining_second")
    val remainingSecond: Int? = null,
    val isSectionWise: Boolean? = null,
    @SerialName("Question")
    val questions: List<QuestionDto>? = null
)

@Serializable
data class QuestionDto(
    @SerialName("_id")
    val id: String? = null,
    val ps: String? = null,
    val description: String? = null,
    val type: String? = null,
    val subject: String? = null,
    val marks: Int? = null,
    val askedIn: List<ExamInfoDto>? = null,
    val topic: String? = null,
    val difficulty: String? = null,
    val questionImage: String? = null,
    @SerialName("op_A")
    val opA: String? = null,
    @SerialName("op_B")
    val opB: String? = null,
    @SerialName("op_C")
    val opC: String? = null,
    @SerialName("op_D")
    val opD: String? = null,
    @SerialName("op_E")
    val opE: String? = null,
    val form: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val negative: Float? = null,
    val selectedOption: String? = null
)
@Serializable
data class ExamInfoDto(
    @SerialName("Exam")
    val exam: String? = null,
    @SerialName("Year")
    val year: Int? = null,
    @SerialName("_id")
    val id: String? = null
)


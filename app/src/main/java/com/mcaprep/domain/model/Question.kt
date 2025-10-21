package com.mcaprep.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Question(
    val id: String?,
    val ps: String?,
    val description: String?,
    val type: String?,
    val subject: String?,
    val marks: Int?,
    val askedIn: List<ExamInfo>?,
    val topic: String?,
    val difficulty: String?,
    val questionImage: String?,
    val solution: String?,
    val solutionImage: String?,
    val explanation: String?,
    val opA: String?,
    val opB: String?,
    val opC: String?,
    val opD: String?,
    val opE: String?,
    val form: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val negative: Float?,
    val selectedOption: String?
): Parcelable

@Parcelize
data class ExamInfo(
    val exam: String?,
    val year: Int?,
    val id: String?
): Parcelable


data class QuestionCountCumulative(
    val total: Int,
    val attempted: Int,
    val correct: Int
)

package com.mcaprep.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResultWithQuestion(
    val result: Result,
    val question: List<Question>
): Parcelable

@Parcelize
data class Result(
    val testId: String,
    val solution: List<Solutions>,
    val totalScore: Int,
    val score: String,
    val count: Int,
    val attempted: String
): Parcelable

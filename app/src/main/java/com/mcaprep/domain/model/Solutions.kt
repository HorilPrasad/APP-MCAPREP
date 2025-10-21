package com.mcaprep.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Solutions(
    val question: String,
    val option: String?
): Parcelable

@Parcelize
data class AnsweredOption(
    val testId: String,
    val questionId: String,
    val selectedOption: String,
    val markForReview: Boolean
): Parcelable




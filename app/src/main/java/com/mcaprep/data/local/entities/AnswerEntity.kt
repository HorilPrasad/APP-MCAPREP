package com.mcaprep.data.local.entities

import androidx.room.Entity

@Entity(tableName = "selected_answers", primaryKeys = ["testId", "questionId"])
data class AnswerEntity(
    val testId: String,
    val questionId: String,
    val selectedOption: String, // e.g., "A", "B", "C", "D", "E"
    val markForReview: Boolean
)

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

@Parcelize
data class TestHistory(
    val detail: Detail,
    val leaderBoard: List<LeaderBoardItem>
): Parcelable

@Parcelize
data class Detail(
    val id: String,
    val name: String,
    val schedule: String,
    val duration: Int,
    val difficulty: String,
    val questions: List<Question>,
    val premium: Boolean,
    val weightage: Weightage,
    val flag: Boolean,
    val isSectionWise: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val totalScore: Int,
    val attempted: String,
    val score: Int,
    val count: Int,
): Parcelable

@Parcelize
data class Weightage(
    val id: String,
    val name: String,
    val subject: List<SubjectItem>,
    val createdAt: String,
    val updatedAt: String,
): Parcelable

@Parcelize
data class SubjectItem(
    val name: String,
    val weight: Double,
    val negative: Double,
    val id: String
): Parcelable

@Parcelize
data class LeaderBoardItem(
    val key: String,
    val value: LeaderBoardValue
): Parcelable

@Parcelize
data class LeaderBoardValue(
    val name: String,
    val marks: Float
): Parcelable

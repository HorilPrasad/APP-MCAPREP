package com.mcaprep.domain.mapper

import com.mcaprep.data.local.entities.AnswerEntity
import com.mcaprep.data.remote.model.EndTestResponse
import com.mcaprep.data.remote.model.ExamInfoDto
import com.mcaprep.data.remote.model.QuestionDto
import com.mcaprep.data.remote.model.ResultDto
import com.mcaprep.data.remote.model.SolutionsDto
import com.mcaprep.data.remote.model.TestDetailsDto
import com.mcaprep.data.remote.model.TestItemDto
import com.mcaprep.domain.model.ExamInfo
import com.mcaprep.domain.model.Question
import com.mcaprep.domain.model.Result
import com.mcaprep.domain.model.ResultWithQuestion
import com.mcaprep.domain.model.Solutions
import com.mcaprep.domain.model.TestDetails
import com.mcaprep.domain.model.TestItem

fun TestItemDto.toDomain() = TestItem(
    id = id,
    name = name,
    schedule = schedule,
    duration = duration,
    difficulty = difficulty,
    premium = premium,
    weightage = weightage,
    createdAt = createdAt,
    updatedAt = updatedAt,
    isSectionWise = isSectionWise,
    questionLength = questionLength,
    attemptedLength = attemptedLength,
    totalAttempt = totalAttempt,
    totalScore = totalScore
)

fun TestDetailsDto.toDomain() = TestDetails(
    id = id ?: "",
    name = name ?: "",
    schedule = schedule ?: "",
    duration = duration ?: 0,
    totalScore = totalScore ?: 0,
    totalAttempt = totalAttempt ?: 0,
    difficulty = difficulty ?: "",
    startTime = startTime ?: "",
    remainingSecond = remainingSecond ?: 0,
    isSectionWise = isSectionWise ?: false,
    questions = questions?.map { it.toDomain() } ?: emptyList()
)

fun QuestionDto.toDomain() = Question(
    id = id,
    ps = ps,
    description = description,
    type = type,
    subject = subject,
    marks = marks,
    askedIn = askedIn?.map { it.toDomain() },
    topic = topic,
    difficulty = difficulty,
    questionImage = questionImage,
    opA = opA,
    opB = opB,
    opC = opC,
    opD = opD,
    opE = opE,
    form = form,
    createdAt = createdAt,
    updatedAt = updatedAt,
    negative = negative,
    selectedOption = selectedOption
)

fun ExamInfoDto.toDomain() = ExamInfo(
    exam = exam,
    year = year,
    id = id
)

fun Solutions.toDto() = SolutionsDto(
    question = question,
    option = option
)

fun AnswerEntity.toDto() = SolutionsDto(
    question = questionId,
    option = selectedOption
)

fun EndTestResponse.toDomain() = ResultWithQuestion(
    result = success.result.toDomain(),
    question = success.questions.map { it.toDomain() }
)

fun ResultDto.toDomain() = Result(
    testId = testId,
    solution = solution.map { it.toDomain() },
    totalScore = totalScore,
    score = score,
    count = count,
    attempted = attempted
)

fun SolutionsDto.toDomain() = Solutions(
    question = question,
    option = option
)


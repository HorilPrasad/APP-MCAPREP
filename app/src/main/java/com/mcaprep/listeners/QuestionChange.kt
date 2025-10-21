package com.mcaprep.listeners

import com.mcaprep.domain.model.Question

interface QuestionChange {
    fun onQuestionChanged(question: Question, position: String)
}
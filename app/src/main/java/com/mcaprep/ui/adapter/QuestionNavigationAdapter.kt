package com.mcaprep.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.mcaprep.R
import com.mcaprep.databinding.ItemQuestionNumberBinding
import com.mcaprep.domain.model.AnsweredOption
import com.mcaprep.domain.model.Question

class QuestionNavigationAdapter(
    private var totalQuestions: MutableList<Question>,
    private var answers: MutableList<AnsweredOption>,
    private val onQuestionClick: (Int) -> Unit
) : RecyclerView.Adapter<QuestionNavigationAdapter.QuestionViewHolder>() {

    inner class QuestionViewHolder(val binding: ItemQuestionNumberBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = ItemQuestionNumberBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return QuestionViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.binding.questionNumber.text = (position+1).toString()
        Log.d("onBindViewHolder", "onBindViewHolder: $answers")
//        if (answers.any { it.questionId == totalQuestions[position].id && it.markForReview}) {
//            holder.binding.questionNumber.setBackgroundColor("#FBC2C2".toColorInt())
//        } else
        if (answers.any { it.questionId == totalQuestions[position].id && it.selectedOption.isNotEmpty() }) {
            holder.binding.questionNumber.isSelected = true
        } else {
            holder.binding.questionNumber.isSelected = false
        }

        holder.binding.questionNumber.setOnClickListener {
            onQuestionClick.invoke(position)
        }
    }

    override fun getItemCount(): Int = totalQuestions.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateQuestionData(newQuestion: MutableList<Question>) {
        Log.d("updateQuestionData", "updateQuestionData: $newQuestion")
        totalQuestions = newQuestion
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAnswerData(newAnswers: MutableList<AnsweredOption>) {
        Log.d("updateAnswerData", "updateAnswerData: $newAnswers")
        answers = newAnswers
        notifyDataSetChanged()
    }
}

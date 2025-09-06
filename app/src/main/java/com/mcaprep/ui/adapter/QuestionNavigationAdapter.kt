package com.mcaprep.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mcaprep.databinding.ItemQuestionNumberBinding

class QuestionNavigationAdapter(
    private val totalQuestions: Int,
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
        holder.binding.questionNumber.setOnClickListener {
            onQuestionClick.invoke(position)
        }
    }

    override fun getItemCount(): Int = totalQuestions
}

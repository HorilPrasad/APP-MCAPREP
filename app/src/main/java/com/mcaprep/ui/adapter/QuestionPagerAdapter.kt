package com.mcaprep.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mcaprep.domain.model.Question
import com.mcaprep.ui.fragment.QuestionFragment

class QuestionPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val questions: List<Question>
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = questions.size
    override fun createFragment(position: Int): Fragment {
        return QuestionFragment.newInstance(questions[position], position.toString())
    }
}
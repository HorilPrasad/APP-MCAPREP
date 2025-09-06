package com.mcaprep.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mcaprep.databinding.ItemTestBinding
import com.mcaprep.domain.model.TestItem
import com.mcaprep.utils.convertSecondsToMinutes
import com.mcaprep.utils.formatUtcToIst

class TestListAdapter(private val testList: List<TestItem>, private val onClickListener: OnClickListeners) : RecyclerView.Adapter<TestListAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        val binding = ItemTestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: ItemViewHolder,
        position: Int
    ) {
        val testItem = testList[position]

        holder.binding.testName.text = testItem.name
        holder.binding.testSchedule.text = formatUtcToIst(testItem.schedule)
        holder.binding.testQuestion.text = "Question ${testItem.questionLength} | ${convertSecondsToMinutes(testItem.duration)} Minutes"
        holder.binding.testMarks.text = "${testItem.totalScore} Marks"
        holder.binding.testAttempt.text = testItem.totalAttempt.toString()
        holder.binding.testPremium.visibility = if (testItem.premium) View.VISIBLE else View.GONE

        holder.binding.root.setOnClickListener {
            onClickListener.onClick(testItem)
        }
    }

    override fun getItemCount(): Int {
        return testList.size
    }

    inner class ItemViewHolder(val binding: ItemTestBinding) :
        RecyclerView.ViewHolder(binding.root)

}

interface OnClickListeners{
    fun onClick(testItem: TestItem)
}
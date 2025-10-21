package com.mcaprep.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mcaprep.R
import com.mcaprep.databinding.ItemLeaderboardBinding
import com.mcaprep.domain.model.RankedUser

class LeaderboardAdapter(
    private val users: List<RankedUser>
): RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LeaderboardViewHolder {
        val binding = ItemLeaderboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LeaderboardViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: LeaderboardViewHolder,
        position: Int
    ) {
        val user = users[position]
        holder.binding.rank.text = user.rank.toString()
        holder.binding.name.text = user.name
        holder.binding.marks.text = user.marks.toString()

        if (user.isCurrentUser) {
            holder.binding.leaderBordRow.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.lightGreen))
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    inner class LeaderboardViewHolder(val binding: ItemLeaderboardBinding) : RecyclerView.ViewHolder(binding.root)


}
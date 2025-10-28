package com.mcaprep.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mcaprep.ui.fragment.SolutionFragment
import com.mcaprep.ui.fragment.PerformanceFragment

class ResultViewPagerAdapter(fragmentActivity: FragmentActivity, private val tabFilters: List<String>) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = tabFilters.size
    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            PerformanceFragment.newInstance()
        } else {
            SolutionFragment.newInstance()
        }
    }
}
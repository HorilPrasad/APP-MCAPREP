package com.mcaprep.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mcaprep.ui.fragment.TestListFragment

class TestSeriesViewPagerAdapter(fragmentActivity: FragmentActivity, private val tabFilters: List<String>, private val testType: String) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = if (tabFilters.isEmpty()) 1 else tabFilters.size

    override fun createFragment(position: Int): Fragment {
        val filter = if (tabFilters.isNotEmpty()) tabFilters[position] else ""
        return TestListFragment.newInstance(filter, testType)
    }
}

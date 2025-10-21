package com.mcaprep.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mcaprep.ui.fragment.TestListFragment

class TestSeriesViewPagerAdapter(fragmentActivity: FragmentActivity, private val tabFilters: List<String>, private val testSeriesName: String) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = tabFilters.size
    override fun createFragment(position: Int): Fragment = TestListFragment.newInstance(tabFilters[position], testSeriesName)
}
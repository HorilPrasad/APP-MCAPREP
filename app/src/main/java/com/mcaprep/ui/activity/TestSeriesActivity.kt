package com.mcaprep.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.mcaprep.databinding.ActivityTestSeriesBinding
import com.mcaprep.ui.adapter.TestSeriesViewPagerAdapter
import com.mcaprep.ui.viewmodel.TestSeriesViewModel
import com.mcaprep.utils.Constants.CUET_MCA
import com.mcaprep.utils.Constants.MAH_CET
import com.mcaprep.utils.Constants.NIMCET
import com.mcaprep.utils.Constants.TANCET
import com.mcaprep.utils.Constants.TEST_NAME
import com.mcaprep.utils.Constants.TEST_TYPE
import com.mcaprep.utils.Constants.TWT
import com.mcaprep.utils.Constants.WBJECA
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestSeriesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTestSeriesBinding
    private val testSeriesViewModel: TestSeriesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTestSeriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val testSeriesName = intent.getStringExtra(TEST_NAME) ?: ""
        val testType = intent.getStringExtra(TEST_TYPE) ?: ""
        binding.toolbar.headerTitle.text = testSeriesName
        binding.toolbar.headerTitle.visibility = View.VISIBLE
        binding.toolbar.actionBarIcon.setOnClickListener {
            onBackPressed()
        }

        val tabs = if (testType == NIMCET || testType == CUET_MCA || testType == MAH_CET || testType == TANCET || testType == WBJECA) {
            listOf("Mocks", "PYQ")
        } else if (testType == TWT) {
            listOf("Mathematics", "Reasoning", "Computer", "English")
        } else {
            emptyList()
        }

        binding.viewPager.adapter = TestSeriesViewPagerAdapter(this, tabs, testType)

        if (tabs.isNotEmpty()) {
            binding.tabLayout.visibility = View.VISIBLE
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                tab.text = tabs[position]
            }.attach()
        } else {
            binding.tabLayout.visibility = View.GONE
        }

        binding.searchView.isIconified = false
        binding.searchView.setIconifiedByDefault(false)
        binding.searchView.clearFocus()

        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean = true.also {
                testSeriesViewModel.searchQuery.value = query ?: ""
            }

            override fun onQueryTextChange(newText: String?): Boolean = true.also {
                testSeriesViewModel.searchQuery.value = newText ?: ""
            }
        })
    }
}

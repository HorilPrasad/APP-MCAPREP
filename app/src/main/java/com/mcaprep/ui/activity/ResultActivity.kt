package com.mcaprep.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.mcaprep.databinding.ActivityResultBinding
import com.mcaprep.ui.adapter.ResultViewPagerAdapter
import com.mcaprep.ui.viewmodel.TestSeriesViewModel
import com.mcaprep.utils.Constants.COUNT
import com.mcaprep.utils.Constants.TEST_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private val testSeriesViewModel: TestSeriesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val testId  = intent.getStringExtra(TEST_ID)
        val count  = intent.getIntExtra(COUNT, 1)

        binding.toolbar.headerTitle.text = "Test Performance"
        binding.toolbar.headerTitle.visibility = View.VISIBLE
        binding.toolbar.actionBarIcon.setOnClickListener {
            onBackPressed()
        }

        if (testId != null) {
            testSeriesViewModel.getTestHistory(testId, count)
        }
    }
}
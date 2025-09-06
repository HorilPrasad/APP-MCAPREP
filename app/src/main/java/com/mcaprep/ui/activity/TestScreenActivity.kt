package com.mcaprep.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.faltenreich.skeletonlayout.createSkeleton
import com.mcaprep.R
import com.mcaprep.databinding.ActivityTestScreenBinding
import com.mcaprep.domain.model.Question
import com.mcaprep.domain.model.TestDetails
import com.mcaprep.ui.adapter.QuestionNavigationAdapter
import com.mcaprep.ui.adapter.QuestionPagerAdapter
import com.mcaprep.ui.viewmodel.TestSeriesViewModel
import com.mcaprep.utils.Constants.TEST_ID
import com.mcaprep.utils.extentions.observeResource
import com.mcaprep.utils.startTimer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTestScreenBinding
    val question: MutableList<Question> = mutableListOf()
    private var testId: String? = null
    private val testSeriesViewModel: TestSeriesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTestScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        testId = intent.getStringExtra(TEST_ID)
        binding.skeletonLayout.createSkeleton()

        testSeriesViewModel.testDetails.observeResource(
            this,
            onSuccess = { testDetails -> onSuccess(testDetails)},
            onError = { error -> showError(error) },
            onLoading = {showProgress()},
        )
        testSeriesViewModel.startExistingTest(testId!!)
        setupButtonClick()
        setupPageChangeListener()

        binding.menuOpen.setOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(binding.navigationDrawer.navView)) {
                binding.drawerLayout.closeDrawer(binding.navigationDrawer.navView,true)
            } else {
                binding.drawerLayout.openDrawer(binding.navigationDrawer.navView,true)
            }
        }
        binding.navigationDrawer.questionNumberRecycler.layoutManager = GridLayoutManager(this,5)


        testSeriesViewModel.endTest.observeResource(
            this,
            onSuccess = { },
            onError = { },
            onLoading = { },
        )
        binding.navigationDrawer.submitButton.setOnClickListener {
            endTest()
        }
    }

    private fun showProgress() {
        binding.drawerLayout.visibility = View.GONE
        binding.loader.visibility = View.VISIBLE
    }

    private fun showError(value: String) {
        binding.drawerLayout.visibility = View.GONE
        binding.loader.visibility = View.GONE
    }

    private fun onSuccess(details: TestDetails) {
        question.addAll(details.questions)
        binding.viewPager.adapter = QuestionPagerAdapter(this, question)
        binding.navigationDrawer.questionNumberRecycler.adapter =
            QuestionNavigationAdapter(question.size) { questionIndex ->
                binding.viewPager.currentItem = questionIndex
//                binding.drawerLayout.closeDrawer(binding.navigationDrawer.navView,true)
            }
        startTimer(details.remainingSecond, binding.remainingTime) {
            // Callback when the timer finishes
        }
        binding.testName.text = details.name
        binding.navigationDrawer.testName.text = details.name
        binding.drawerLayout.visibility = View.VISIBLE
        binding.loader.visibility = View.GONE
    }

    private fun setupButtonClick() {
        binding.previous.setOnClickListener {
            val prevPosition = binding.viewPager.currentItem - 1
            if (prevPosition >= 0) {
                binding.viewPager.currentItem = prevPosition
            }
        }

        binding.saveNNextBtn.setOnClickListener {
            val nextPosition = binding.viewPager.currentItem + 1
            if (nextPosition < question.size) {
                binding.viewPager.currentItem = nextPosition
            }
        }
    }

    private fun setupPageChangeListener() {
        binding.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                // Update button states
                binding.previous.isEnabled = position > 0
                binding.saveNNextBtn.isEnabled = position < question.size - 1
            }
        })
    }

    private fun endTest() {
        testSeriesViewModel.endTest(testId!!, emptyList())
    }
}
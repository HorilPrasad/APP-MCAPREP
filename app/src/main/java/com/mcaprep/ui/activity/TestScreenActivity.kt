package com.mcaprep.ui.activity

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.faltenreich.skeletonlayout.createSkeleton
import com.mcaprep.data.remote.model.EndTestResponse
import com.mcaprep.databinding.ActivityTestScreenBinding
import com.mcaprep.domain.model.Question
import com.mcaprep.domain.model.ResultWithQuestion
import com.mcaprep.domain.model.TestDetails
import com.mcaprep.ui.adapter.QuestionNavigationAdapter
import com.mcaprep.ui.adapter.QuestionPagerAdapter
import com.mcaprep.ui.fragment.ResultDialog
import com.mcaprep.ui.fragment.SubmitTestDialog
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
    private var timer: CountDownTimer? = null
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
        testSeriesViewModel.initializeTest(testId!!)
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
            onSuccess = { result -> onTestSubmited(result)},
            onError = { error ->  submitError(error)},
            onLoading = { binding.loader.visibility = View.VISIBLE },
        )
        binding.navigationDrawer.submitButton.setOnClickListener {
            binding.drawerLayout.closeDrawer(binding.navigationDrawer.navView,true)
            endTest()
        }
    }

    private fun submitError(error: String) {
        Log.e("error", "onCreate: $error" )
        binding.loader.visibility = View.GONE
    }

    private fun onTestSubmited(result: ResultWithQuestion) {
        binding.loader.visibility = View.GONE
        timer?.cancel()
        val resultDialog = ResultDialog.newInstance(result.result)
        resultDialog.show(supportFragmentManager, "ResultDialog")
    }

    private fun showProgress() {
        binding.testScreenLayout.visibility = View.GONE
        binding.loader.visibility = View.VISIBLE
    }

    private fun showError(value: String) {
        binding.testScreenLayout.visibility = View.GONE
        binding.loader.visibility = View.GONE
        Toast.makeText(this, value, Toast.LENGTH_SHORT).show()
    }

    private fun onSuccess(details: TestDetails) {
        question.addAll(details.questions)
        binding.viewPager.adapter = QuestionPagerAdapter(this, question)
        binding.navigationDrawer.questionNumberRecycler.adapter =
            QuestionNavigationAdapter(question.size) { questionIndex ->
                binding.viewPager.currentItem = questionIndex
//                binding.drawerLayout.closeDrawer(binding.navigationDrawer.navView,true)
            }
        timer = startTimer(details.remainingSecond, binding.remainingTime) {
            endTest()
        }
        binding.testName.text = details.name
        binding.navigationDrawer.testName.text = details.name
        binding.testScreenLayout.visibility = View.VISIBLE
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
        val dialog = SubmitTestDialog.newInstance()
        dialog.show(supportFragmentManager, "SubmitTestDialog")
    }
}
package com.mcaprep.ui.activity

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.faltenreich.skeletonlayout.createSkeleton
import com.mcaprep.databinding.ActivityTestScreenBinding
import com.mcaprep.domain.mapper.toDomain
import com.mcaprep.domain.model.AnsweredOption
import com.mcaprep.domain.model.Question
import com.mcaprep.domain.model.ResultWithQuestion
import com.mcaprep.domain.model.TestDetails
import com.mcaprep.listeners.QuestionChange
import com.mcaprep.ui.adapter.QuestionNavigationAdapter
import com.mcaprep.ui.fragment.QuestionFragment
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
    val questions: MutableList<Question> = mutableListOf()
    private var testId: String? = null
    private var timer: CountDownTimer? = null
    private val testSeriesViewModel: TestSeriesViewModel by viewModels()
    private var selectedOption: MutableList<AnsweredOption> = mutableListOf()
    private lateinit var questionNavigationAdapter: QuestionNavigationAdapter
    private var questionChangeListener: QuestionChange? = null
    private var currentPosition: Int = 0
    private var attempted: Int = 0
    private var markForReview: Int = 0


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
        updateButtonUI()

        testSeriesViewModel.testDetails.observeResource(
            this,
            onSuccess = { testDetails -> onSuccess(testDetails)},
            onError = { error -> showError(error) },
            onLoading = {showProgress()},
        )
        questionNavigationAdapter = QuestionNavigationAdapter(questions, selectedOption) { questionIndex ->
//            binding.viewPager.currentItem = questionIndex
            currentPosition  = questionIndex
            updateButtonUI()
            questionChangeListener?.onQuestionChanged(questions[currentPosition], (currentPosition + 1).toString())
                binding.drawerLayout.closeDrawer(binding.navigationDrawer.navView,true)
        }
        testSeriesViewModel.answers.observe(
            this,
            { answers ->
                selectedOption.clear()
                selectedOption.addAll(answers.map { it.toDomain() })
                questionNavigationAdapter.updateAnswerData(selectedOption)
                attempted = answers.filter { it.selectedOption.isNotEmpty() }.size
                markForReview = answers.filter { it.markForReview }.size
                binding.navigationDrawer.attemptedQuestion.text = attempted.toString()
                binding.navigationDrawer.markedForReviewQuestion.text = markForReview.toString()
                binding.navigationDrawer.unattemptedQuestion.text = (questions.size - (attempted + markForReview)).toString()
            }
        )
        testSeriesViewModel.initializeTest(testId!!)
        testSeriesViewModel.startExistingTest(testId!!)
        testSeriesViewModel.loadAnswersForTest(testId!!)
        setupButtonClick()
//        setupPageChangeListener()

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
            showSubmitTestDialog()
        }


        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(binding.navigationDrawer.navView)) {
                    binding.drawerLayout.closeDrawer(binding.navigationDrawer.navView,true)
                } else {
                    showSubmitTestDialog()
                }
            }

        })
    }

    private fun submitError(error: String) {
        Log.e("error", "onCreate: $error" )
        binding.loader.visibility = View.GONE
    }

    private fun onTestSubmited(result: ResultWithQuestion) {
        binding.loader.visibility = View.GONE
        timer?.cancel()
        val resultDialog = ResultDialog.newInstance(result.result)
        testSeriesViewModel.clearAnswersForCurrentTest()
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
        questions.clear()
        questions.addAll(details.questions)
//        binding.viewPager.adapter = QuestionPagerAdapter(this, question)
        val questionFragment = QuestionFragment.newInstance(questions[0], 0.toString())
        supportFragmentManager.beginTransaction()
            .replace(binding.questionFragmentContainer.id, questionFragment).commit()
        binding.navigationDrawer.questionNumberRecycler.adapter = questionNavigationAdapter
        timer = startTimer(details.remainingSecond, binding.remainingTime) {
            testSeriesViewModel.endTest()
        }
        binding.navigationDrawer.totalQuestion.text = questions.size.toString()
        binding.navigationDrawer.unattemptedQuestion.text = (questions.size - (attempted + markForReview)).toString()
        binding.testName.text = details.name
        binding.navigationDrawer.testName.text = details.name
        binding.testScreenLayout.visibility = View.VISIBLE
        binding.loader.visibility = View.GONE
    }

    private fun setupButtonClick() {
        binding.previous.setOnClickListener {
            if (currentPosition > 0) {
                currentPosition--
                updateButtonUI()
                questionChangeListener?.onQuestionChanged(
                    questions[currentPosition],
                    (currentPosition + 1).toString()
                )
            }
//            val prevPosition = binding.viewPager.currentItem - 1
//            if (prevPosition >= 0) {
//                binding.viewPager.currentItem = prevPosition
//            }
        }

        binding.saveNNextBtn.setOnClickListener {
            if (currentPosition < questions.size - 1) {
                currentPosition++
                updateButtonUI()
                questionChangeListener?.onQuestionChanged(
                    questions[currentPosition],
                    (currentPosition + 1).toString()
                )
            }
//            val currentPosition = binding.viewPager.currentItem
//            if (currentPosition == question.size - 1) {
//                showSubmitTestDialog()
//            } else {
//                val nextPosition = currentPosition + 1
//                if (nextPosition < question.size) {
//                    binding.viewPager.currentItem = nextPosition
//                }
//            }
        }
    }

//    private fun setupPageChangeListener() {
//        binding.viewPager.registerOnPageChangeCallback(object :
//            ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                // Update button states
//                binding.previous.visibility = if (position == 0) View.GONE else View.VISIBLE
//
//                if (position == question.size - 1) {
//                    binding.saveNNextBtn.text = "Submit"
//                }
//            }
//        })
//    }

    private fun showSubmitTestDialog() {
        val dialog = SubmitTestDialog.newInstance()
        dialog.show(supportFragmentManager, "SubmitTestDialog")
    }

    internal fun setQuestionChangeListener(listener: QuestionChange) {
        questionChangeListener = listener
    }

    private fun updateButtonUI() {
        binding.previous.visibility = if (currentPosition == 0) View.GONE else View.VISIBLE
        binding.saveNNextBtn.text = if (currentPosition == questions.size - 1) "Submit" else "Save & Next"
    }
}
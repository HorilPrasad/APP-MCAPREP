package com.mcaprep.ui.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.mcaprep.databinding.FragmentQuestionBinding
import com.mcaprep.domain.model.Question
import com.mcaprep.listeners.QuestionChange
import com.mcaprep.ui.activity.TestScreenActivity
import com.mcaprep.ui.viewmodel.TestSeriesViewModel
import com.mcaprep.ui.views.MathJaxView
import com.mcaprep.utils.MathJaxInterface
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val ARG_QUESTION = "question_arg"
private const val ARG_POSITION = "position_arg"

@AndroidEntryPoint
class QuestionFragment : Fragment(), MathJaxInterface, QuestionChange {

    private var question: Question? = null
    private var questionPosition: String? = null


    private var _binding: FragmentQuestionBinding? = null
    private val binding get() = _binding!!

    private val testSeriesViewModel: TestSeriesViewModel by activityViewModels()

    // Store option layouts for easier management
    private lateinit var optionLayouts: Map<String, LinearLayoutCompat>
    private lateinit var options: Map<String, MathJaxView>
    private lateinit var checkedLayout: Map<String, ImageView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            question = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(ARG_QUESTION, Question::class.java)
            } else {
                it.getParcelable(ARG_QUESTION)
            }
            questionPosition = it.getString(ARG_POSITION)
        }
        (activity as TestScreenActivity).setQuestionChangeListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the question

        val currentQuestion = question ?: return

        // Initialize the map of option layouts
        optionLayouts = mapOf(
            "op_A" to binding.opALayout,
            "op_B" to binding.opBLayout,
            "op_C" to binding.opCLayout,
            "op_D" to binding.opDLayout,
            "op_E" to binding.opELayout
        )

        options = mapOf(
            "op_A" to binding.opA,
            "op_B" to binding.opB,
            "op_C" to binding.opC,
            "op_D" to binding.opD,
            "op_E" to binding.opE
        )
        checkedLayout = mapOf(
            "op_A" to binding.checkedOpA,
            "op_B" to binding.checkedOpB,
            "op_C" to binding.checkedOpC,
            "op_D" to binding.checkedOpD,
            "op_E" to binding.checkedOpE
        )

        // Ensure testId is initialized in ViewModel
        // This might be better handled if testId is guaranteed to be set before fragments are shown
        testSeriesViewModel.initializeTest(testSeriesViewModel.currentTestId ?: "")

        updateUI(currentQuestion, (questionPosition?.toInt()?.plus(1)).toString())
    }

    private fun updateUI(currentQuestion: Question, position: String) {
        binding.questionNumber.text = position
        // Set question text and options
        currentQuestion.ps?.takeIf { it.isNotBlank() }?.let { binding.questionText.setText(it) }
        currentQuestion.opA?.takeIf { it.isNotBlank() }?.let { binding.opA.setText(it) }
        currentQuestion.opB?.takeIf { it.isNotBlank() }?.let { binding.opB.setText(it) }
        currentQuestion.opC?.takeIf { it.isNotBlank() }?.let { binding.opC.setText(it) }
        currentQuestion.opD?.takeIf { it.isNotBlank() }?.let { binding.opD.setText(it) }

        if (!currentQuestion.opE.isNullOrBlank()) {
            binding.opE.setText(currentQuestion.opE)
            binding.opELayout.visibility = View.VISIBLE
        } else {
            binding.opELayout.visibility = View.GONE
        }

        // Load previously selected answer and update UI
        lifecycleScope.launch {
            val selectedOptionKey = currentQuestion.id?.let { testSeriesViewModel.getSelectedAnswerForQuestion(it) }
            updateOptionSelectionVisuals(selectedOptionKey)
        }

        // Set click listeners for each option layout
        options.forEach { (key, layout) ->
            layout.setOnClickListener {
                currentQuestion.id?.let { questionId -> handleOptionClick(questionId, key) }
            }
        }

//        binding.markForReview.setOnCheckedChangeListener { view, isChecked ->
//            if (isChecked) {
//                currentQuestion.id?.let { testSeriesViewModel.markForReviewAnswer(it, true) }
//            } else {
//                currentQuestion.id?.let { testSeriesViewModel.markForReviewAnswer(it, false) }
//            }
//        }
    }

    private fun handleOptionClick(questionId: String, selectedKey: String) {
        updateOptionSelectionVisuals(selectedKey)
        testSeriesViewModel.saveSelectedAnswer(questionId, selectedKey)
    }

    private fun updateOptionSelectionVisuals(selectedKey: String?) {
        optionLayouts.forEach { (key, layout) ->
            layout.isSelected = (key == selectedKey)
        }
        checkedLayout.forEach { (key, imageView) ->
            imageView.visibility = if (key == selectedKey) View.VISIBLE else View.GONE
        }
    }

    override fun onMathJaxReady() {
        // Re-render if necessary
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onQuestionChanged(
        question: Question,
        position: String
    ) {
        updateUI(question, position)
    }

    companion object {
        @JvmStatic
        fun newInstance(questionData: Question, positionStr: String) =
            QuestionFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_QUESTION, questionData)
                    putString(ARG_POSITION, positionStr)
                }
            }
    }
}
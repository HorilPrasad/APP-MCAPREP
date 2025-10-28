package com.mcaprep.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.activityViewModels
import com.mcaprep.R
import com.mcaprep.databinding.FragmentSolutionBinding
import com.mcaprep.domain.model.Question
import com.mcaprep.domain.model.TestHistory
import com.mcaprep.ui.viewmodel.TestSeriesViewModel
import com.mcaprep.utils.extentions.observeResource
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class SolutionFragment : Fragment() {

    private var _binding: FragmentSolutionBinding? = null
    private val binding get() = _binding!!
    private val testSeriesViewModel: TestSeriesViewModel by activityViewModels()

    private var currentPosition: Int = 0
    private var currentQuestion: Question? = null
    val questions: MutableList<Question> = mutableListOf()

    private lateinit var optionLayouts: Map<String, LinearLayoutCompat>
    private lateinit var checkedLayout: Map<String, AppCompatImageView>
    private lateinit var optionKeys: Map<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSolutionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        optionLayouts = mapOf(
//            "op_A" to binding.question.opALayout,
//            "op_B" to binding.question.opBLayout,
//            "op_C" to binding.question.opCLayout,
//            "op_D" to binding.question.opDLayout,
//            "op_E" to binding.question.opELayout
//        )
//        checkedLayout = mapOf(
//            "op_A" to binding.question.checkedOpA,
//            "op_B" to binding.question.checkedOpB,
//            "op_C" to binding.question.checkedOpC,
//            "op_D" to binding.question.checkedOpD,
//            "op_E" to binding.question.checkedOpE
//        )
//        optionKeys = mapOf(
//            "op_A" to "A",
//            "op_B" to "B",
//            "op_C" to "C",
//            "op_D" to "D",
//            "op_E" to "E"
//        )
//        binding.question.solutionLayout.visibility = View.VISIBLE

        binding.saveNNextBtn.text = "Next"
        setupButtonClick()
        updateButtonUI()
        testSeriesViewModel.testHistory.observeResource(
            this,
            onSuccess = { onSuccess(it) },
            onError = { onError(it) },
            onLoading = { onLoading() }
        )
    }

    private fun onSuccess(data: TestHistory) {
//        binding.main.visibility = View.VISIBLE
        binding.loader.visibility = View.GONE
        questions.clear()
        questions.addAll(data.detail.questions)
        currentQuestion = questions[currentPosition]
//        updateUI(questions[currentPosition], (currentPosition + 1).toString())
        showQuestionFragment(currentQuestion!!, currentPosition)
    }

    private fun onLoading() {
//        binding.main.visibility = View.GONE
        binding.loader.visibility = View.VISIBLE
    }

    private fun onError(error: String) {
//        binding.main.visibility = View.VISIBLE
        binding.loader.visibility = View.GONE
    }

    private fun setupButtonClick() {
        binding.previous.setOnClickListener {
            if (currentPosition > 0) {
                currentPosition--
                updateButtonUI()
//                updateUI(questions[currentPosition], (currentPosition + 1).toString())
                showQuestionFragment(currentQuestion!!, currentPosition)
            }
        }

        binding.saveNNextBtn.setOnClickListener {
            if (currentPosition < questions.size - 1) {
                currentPosition++
                updateButtonUI()
//                updateUI(questions[currentPosition], (currentPosition + 1).toString())
                showQuestionFragment(questions[currentPosition], currentPosition)
            }
        }
    }
    private fun updateButtonUI() {
        binding.previous.visibility = if (currentPosition == 0) View.GONE else View.VISIBLE
        binding.saveNNextBtn.visibility = if (currentPosition == questions.size - 1) View.GONE else View.VISIBLE
    }

    @SuppressLint("SetTextI18n")
//    private fun updateUI(currentQuestion: Question, position: String) {
//        binding.question.questionNumber.text = position
//        // Set question text and options
//        currentQuestion.ps?.takeIf { it.isNotBlank() }?.let { binding.question.questionText.setText(it) }
//        currentQuestion.opA?.takeIf { it.isNotBlank() }?.let { binding.question.opA.setText(it) }
//        currentQuestion.opB?.takeIf { it.isNotBlank() }?.let { binding.question.opB.setText(it) }
//        currentQuestion.opC?.takeIf { it.isNotBlank() }?.let { binding.question.opC.setText(it) }
//        currentQuestion.opD?.takeIf { it.isNotBlank() }?.let { binding.question.opD.setText(it) }
//
//        if (!currentQuestion.opE.isNullOrBlank()) {
//            binding.question.opE.setText(currentQuestion.opE)
//            binding.question.opELayout.visibility = View.VISIBLE
//        } else {
//            binding.question.opELayout.visibility = View.GONE
//        }
//
//        binding.question.correctOption.text = "Correct Option: ${optionKeys[currentQuestion.solution]}"
//        updateOptionSelectedAndCorrect(currentQuestion.solution)
//        currentQuestion.explanation?.let { binding.question.solutionText.setText(it) }
//        if (!currentQuestion.selectedOption.isNullOrEmpty()){
//            if (currentQuestion.selectedOption != currentQuestion.solution) {
//                updateOptionIncorrect(currentQuestion.selectedOption)
//                binding.question.incorrectMarks.text = "- ${currentQuestion.negative}"
//                binding.question.incorrectMarks.visibility = View.VISIBLE
//                binding.question.correctMarks.visibility = View.GONE
//            } else {
//                binding.question.correctMarks.text = "+ ${currentQuestion.marks}"
//                binding.question.correctMarks.visibility = View.VISIBLE
//                binding.question.incorrectMarks.visibility = View.GONE
//            }
//        }
//    }

    private fun updateOptionSelectedAndCorrect(selectedKey: String?) {
        optionLayouts.forEach { (key, layout) ->
            layout.isSelected = (key == selectedKey)
        }
        checkedLayout.forEach { (key, imageView) ->
            imageView.visibility = if (key == selectedKey) View.VISIBLE else View.GONE
        }
    }

    private fun updateOptionIncorrect(selectedKey: String?) {
        optionLayouts.forEach { (key, layout) ->
            if (key == selectedKey) {
                layout.setBackgroundResource(R.drawable.border_radius_red)
            }
        }
        checkedLayout.forEach { (key, imageView) ->
            if (key == selectedKey) {
                imageView.visibility = View.VISIBLE
                imageView.setImageResource(R.drawable.ic_incorrect)
            }
        }
    }

    private fun showQuestionFragment(currentQuestion: Question, position: Int) {
        val questionFragment = QuestionFragment.newInstance(currentQuestion, (position + 1).toString(), true)
        requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            .replace(binding.questionFragmentContainer.id, questionFragment)
            .commit()
    }


    companion object {
        @JvmStatic
        fun newInstance() = SolutionFragment()
    }
}
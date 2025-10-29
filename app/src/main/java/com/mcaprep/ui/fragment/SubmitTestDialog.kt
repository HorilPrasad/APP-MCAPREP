package com.mcaprep.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.mcaprep.databinding.DialogSubmitBinding
import com.mcaprep.ui.viewmodel.TestSeriesViewModel
import kotlin.getValue
import androidx.core.graphics.drawable.toDrawable

private const val ARG_TOTAL_QUESTIONS = "totalQuestions"
private const val ARG_ATTEMPTED = "attempted"
private const val ARG_MARK_FOR_REVIEW = "markForReview"
class SubmitTestDialog : DialogFragment() {
    private var totalQuestions: Int = 0
    private var attempted: Int = 0
    private var markForReview: Int = 0

    private var _binding: DialogSubmitBinding? = null
    private val binding get() = _binding!!

    private val testSeriesViewModel: TestSeriesViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            totalQuestions = it.getInt(ARG_TOTAL_QUESTIONS)
            attempted = it.getInt(ARG_ATTEMPTED)
            markForReview = it.getInt(ARG_MARK_FOR_REVIEW)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DialogSubmitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        dialog?.setCanceledOnTouchOutside(false)

        binding.totalQuestion.text = totalQuestions.toString()
        binding.attempted.text = attempted.toString()
        binding.markedForReview.text = markForReview.toString()
        binding.unattempted.text = (totalQuestions - attempted - markForReview).toString()

        binding.ivClose.setOnClickListener {
            dismiss()
        }
        binding.buttonSubmit.setOnClickListener {
            testSeriesViewModel.endTest()
            dismiss()
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(totalQuestions: Int, attempted: Int, markForReview: Int) = SubmitTestDialog().apply {
            arguments = Bundle().apply {
                putInt(ARG_TOTAL_QUESTIONS, totalQuestions)
                putInt(ARG_ATTEMPTED, attempted)
                putInt(ARG_MARK_FOR_REVIEW, markForReview)
            }
        }
    }
}
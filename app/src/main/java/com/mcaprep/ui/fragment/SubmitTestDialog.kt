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


class SubmitTestDialog : DialogFragment() {

    private var _binding: DialogSubmitBinding? = null
    private val binding get() = _binding!!

    private val testSeriesViewModel: TestSeriesViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        fun newInstance() = SubmitTestDialog()
    }
}
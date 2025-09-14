package com.mcaprep.ui.fragment

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.core.graphics.toColorInt
import androidx.fragment.app.DialogFragment
import com.mcaprep.databinding.DialogResultBinding
import com.mcaprep.domain.model.Result

class ResultDialog : DialogFragment() {
    private var _binding: DialogResultBinding? = null
    private val binding get() = _binding!!

    private var result: Result? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val result = it.getParcelable<Result>("result")
            this.result = result
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DialogResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        dialog?.setCanceledOnTouchOutside(false)
        binding.totalScore.text = "/${result?.totalScore}"
        binding.score.text = result?.score
        binding.attempted.text = result?.solution?.size.toString()

        result?.score?.toFloat()?.let {
            if (it > 0) {
                binding.score.setTextColor("#54A156".toColorInt())
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    companion object {
        @JvmStatic
        fun newInstance(result: Result) = ResultDialog().apply {
            arguments = Bundle().apply {
                putParcelable("result", result)
            }
        }
    }
}
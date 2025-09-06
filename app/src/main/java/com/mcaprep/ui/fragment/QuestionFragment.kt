package com.mcaprep.ui.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mcaprep.databinding.FragmentQuestionBinding
import com.mcaprep.domain.model.Question
import com.mcaprep.utils.MathJaxInterface
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [QuestionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class QuestionFragment : Fragment(), MathJaxInterface {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var question: Question? = null

    private var _binding: FragmentQuestionBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        question = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(ARG_PARAM1, Question::class.java)
        } else {
            arguments?.getParcelable<Question>(ARG_PARAM1)
        }
        param2 = arguments?.getString(ARG_PARAM2)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ps = question?.ps
        val opA = question?.opA
        val opB = question?.opB
        val opC = question?.opC
        val opD = question?.opD
        val opE = question?.opE

        if (!ps.isNullOrBlank()) {
            binding.questionText.setText(ps)
        }
        if (!opA.isNullOrBlank()) {
            binding.opA.setText(opA)
        }
        if (!opB.isNullOrBlank()) {
            binding.opB.setText(opB)
        }
        if (!opC.isNullOrBlank()) {
            binding.opC.setText(opC)
        }
        if (!opD.isNullOrBlank()) {
            binding.opD.setText(opD)
        }
        if (!opE.isNullOrBlank()) {
            binding.opE.setText(opE)
        }else {
            binding.opELayout.visibility = View.GONE
        }

        binding.questionNumber.text = param2

    }

    override fun onMathJaxReady() {

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment QuestionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Question, position: String) =
            QuestionFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, position)
                }
            }
    }
}
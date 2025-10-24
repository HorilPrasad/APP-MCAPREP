package com.mcaprep.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textview.MaterialTextView
import com.mcaprep.R

class ExamInstructionsFragment : Fragment(R.layout.fragment_exam_instructions) {

    private lateinit var titleText: MaterialTextView
    private lateinit var instructionsText: MaterialTextView
    private lateinit var nextButton: MaterialButton
    private lateinit var previousButton: MaterialButton
    private lateinit var startButton: MaterialButton
    private lateinit var agreeCheckBox: MaterialCheckBox

    private var currentStep = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        titleText = view.findViewById(R.id.instructions_title)
        instructionsText = view.findViewById(R.id.instructions_text)
        nextButton = view.findViewById(R.id.next_button)
        previousButton = view.findViewById(R.id.previous_button)
        startButton = view.findViewById(R.id.start_test_button)
        agreeCheckBox = view.findViewById(R.id.agree_checkbox)

        showFirstInstructions()

        nextButton.setOnClickListener { showSecondInstructions() }
        previousButton.setOnClickListener { showFirstInstructions() }
        agreeCheckBox.setOnCheckedChangeListener { _, isChecked -> startButton.isEnabled = isChecked }
        startButton.setOnClickListener {
            // TODO: Launch test activity or navigate to test fragment
        }
    }

    private fun showFirstInstructions() {
        currentStep = 1
        titleText.text = "General & Navigational Instructions"
        previousButton.visibility = View.GONE
        nextButton.visibility = View.VISIBLE
        agreeCheckBox.visibility = View.GONE
        startButton.visibility = View.GONE

        instructionsText.text = """
            ⏰ **General Instructions:**
            • The clock will be set at the server.
            • The countdown timer in the top-right corner shows the remaining time.
            • When the timer reaches zero, the test will auto-submit.
            
            🧭 **Navigational Instructions:**
            • BLUE: Question opened  
            • GREEN: Question answered  
            • WHITE: Not visited  
            • RED: Marked for review
        """.trimIndent()
    }

    private fun showSecondInstructions() {
        currentStep = 2
        titleText.text = "Exam Specific Instructions"
        previousButton.visibility = View.VISIBLE
        nextButton.visibility = View.GONE
        agreeCheckBox.visibility = View.VISIBLE
        startButton.visibility = View.VISIBLE
        startButton.isEnabled = false

        instructionsText.text = """
            📘 **Exam Specific Instructions:**
            • This is a NIMCET 2024 Official Online Exam.  
            • Duration: 120 Minutes  
            • Total Questions: 120  
            • Total Score: 1000 Marks  

            **Marking Scheme:**
            • Mathematics: +12 / -3  
            • Logical Reasoning: +6 / -1.5  
            • Computer Awareness: +6 / -1.5  
            • English: +4 / -1  

            • Unattempted Questions: 0 Marks  
            • Paper shown here is for practice purposes only.  
            • Each MCQ will have 4–5 options and only one correct answer.
        """.trimIndent()
    }
}

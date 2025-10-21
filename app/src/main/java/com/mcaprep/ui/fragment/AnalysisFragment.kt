package com.mcaprep.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mcaprep.R
import com.mcaprep.data.local.PrefManager
import com.mcaprep.databinding.FragmentAnalysisBinding
import com.mcaprep.domain.model.LeaderBoardItem
import com.mcaprep.domain.model.Question
import com.mcaprep.domain.model.QuestionCountCumulative
import com.mcaprep.domain.model.RankedUser
import com.mcaprep.domain.model.TestHistory
import com.mcaprep.ui.adapter.LeaderboardAdapter
import com.mcaprep.ui.viewmodel.TestSeriesViewModel
import com.mcaprep.utils.extentions.observeResource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.getValue


/**
 * A simple [Fragment] subclass.
 * Use the [AnalysisFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class AnalysisFragment : Fragment() {

    @Inject
    lateinit var pref: PrefManager
    private var _binding: FragmentAnalysisBinding? = null
    private val binding get() = _binding!!
    private var rank = 0
    private var totalRank = 0

    private val testSeriesViewModel: TestSeriesViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAnalysisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        testSeriesViewModel.testHistory.observeResource(
            this,
            onSuccess = { onSuccess(it) },
            onError = { onError(it) },
            onLoading = { onLoading() }
        )
    }
    @SuppressLint("SetTextI18n")
    private fun onSuccess(data: TestHistory) {
        Log.e("AnalysisFragment", "onSuccess: ")
        binding.main.visibility = View.VISIBLE
        binding.loader.visibility = View.GONE

        val questionCountCumulative = processQuestionCumulative(data.detail.questions)
        Log.i("AnalysisFragment", "onSuccess: $questionCountCumulative")
        binding.score.text = data.detail.score.toString()
        binding.totalScore.text = "/${data.detail.totalScore}"
        binding.totalQuestion.text = "/${data.detail.questions.size}"
        binding.attempted.text = questionCountCumulative.attempted.toString()

        val leaderboardData = processLeaderboardData(data.leaderBoard, pref.getUserId())
        val leaderboardAdapter = LeaderboardAdapter(leaderboardData)
        Log.i("AnalysisFragment", "onSuccess: $leaderboardData")
        binding.leaderBordRecycler.adapter = leaderboardAdapter

        binding.rank.text = rank.toString()
        binding.allRank.text = "/$totalRank"

        if (questionCountCumulative.attempted > 0) {
            binding.accuracy.text = "${(questionCountCumulative.correct / questionCountCumulative.attempted) * 100}%"
        } else {
            binding.accuracy.text = "NA"
        }
    }

    private fun onLoading() {
        Log.e("AnalysisFragment", "onLoading: ")
        binding.main.visibility = View.GONE
        binding.loader.visibility = View.VISIBLE
    }

    private fun onError(error: String) {
        Log.e("AnalysisFragment", "onError: $error")
        binding.main.visibility = View.VISIBLE
        binding.loader.visibility = View.GONE
    }

    private fun processQuestionCumulative(question: List<Question>): QuestionCountCumulative {
        var total = 0
        var correct = 0
        var attempted = 0

        question.forEach {
            total++
            if (!it.selectedOption.isNullOrEmpty()) {
                attempted++
                if (it.solution == it.selectedOption)
                    correct++
            }
        }
        return QuestionCountCumulative(total, attempted, correct)
    }

    private fun processLeaderboardData(
        leaderboard: List<LeaderBoardItem>,
        currentUserId: String
    ): List<RankedUser> {
        Log.i("AnalysisFragment", "processLeaderboardData: $currentUserId")
        // 1. Sort users by marks in descending order.
        val sortedByMarks = leaderboard.sortedByDescending { it.value.marks }

        // 2. Assign ranks, correctly handling ties (users with same marks get same rank).
        val rankedList = mutableListOf<RankedUser>()
        var currentRank = 0
        var lastMarks: Float = (-1.0).toFloat()
        sortedByMarks.forEachIndexed { index, entry ->
            // If the marks are different from the previous user, update the rank
            if (entry.value.marks != lastMarks) {
                currentRank = index + 1
            }
            rankedList.add(
                RankedUser(
                    rank = currentRank,
                    name = entry.value.name,
                    marks = entry.value.marks,
                    isCurrentUser = entry.key == currentUserId
                )
            )
            lastMarks = entry.value.marks
        }

        // 3. Find the current user and separate them from the list.
        val currentUser = rankedList.find { it.isCurrentUser }
        val otherUsers = rankedList.filter { !it.isCurrentUser }

        // 4. Create the final list for the adapter.
        val finalList = mutableListOf<RankedUser>()
        currentUser?.let {
            finalList.add(it) // Add the current user to the top
        }
        finalList.addAll(otherUsers) // Add the rest of the users
        rank = currentUser?.rank ?: 0
        totalRank = finalList.size
        return finalList
    }

    companion object {
        @JvmStatic
        fun newInstance() = AnalysisFragment()
    }
}
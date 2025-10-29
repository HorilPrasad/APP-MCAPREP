package com.mcaprep.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.mcaprep.databinding.FragmentTestListBinding
import com.mcaprep.domain.model.TestItem
import com.mcaprep.ui.adapter.OnClickListeners
import com.mcaprep.ui.adapter.TestListAdapter
import com.mcaprep.ui.viewmodel.TestSeriesViewModel
import com.mcaprep.utils.Constants.CUET_MCA
import com.mcaprep.utils.Constants.MAH_CET
import com.mcaprep.utils.Constants.NIMCET
import com.mcaprep.utils.Constants.TANCET
import com.mcaprep.utils.Constants.TWT
import com.mcaprep.utils.Constants.WBJECA
import com.mcaprep.utils.NavigationHelper
import com.mcaprep.utils.extentions.observeResource
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_FILTER = "filter"
private const val ARG_TEST_TYPE = "testType"


@AndroidEntryPoint
class TestListFragment : Fragment(), OnClickListeners {
    private var filter: String? = null
    private var testType: String? = null
    private var _binding: FragmentTestListBinding? = null
    private val binding get() = _binding!!
    private val testSeriesViewModel: TestSeriesViewModel by viewModels()
    private var originalItems: List<TestItem> = listOf()
    private var currentTestId: String? = null
    private var testName: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            filter = it.getString(ARG_FILTER)
            testType = it.getString(ARG_TEST_TYPE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTestListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        testSeriesViewModel.testSeries.observeResource(
            owner = viewLifecycleOwner,
            onLoading = { showLoading() },
            onSuccess = { testItems -> showTestItems(testItems) },
            onError = { errorMsg -> showError(errorMsg) }
        )

        testSeriesViewModel.searchQuery.observe(viewLifecycleOwner) { query ->
            Log.d("TestListFragment", "Search Query: $query")
            filterData(query)
        }
        when (testType) {
            NIMCET -> {
                if (filter == "Mocks")
                    testName = "SWMOCK"
                else
                    testName = "SWPYQ"
            }
            CUET_MCA , MAH_CET , TANCET, WBJECA -> {
                if (filter == "Mocks")
                    testName = "${testType}MOCK"
                else
                    testName = "${testType}PYQ"
            }
            TWT -> {
                if (filter == "Mathematics")
                    testName = "TWTM"
                else if (filter == "English")
                    testName = "TWTE"
                else if (filter == "Reasoning")
                    testName = "TWTR"
                else if (filter == "Computer")
                    testName = "TWTC"
            }
            else -> {
                testName = testType.toString()
            }
        }
        testSeriesViewModel.getTestSeries(testName)

        testSeriesViewModel.activeTest.observeResource(
            owner = viewLifecycleOwner,
            onLoading = { showLoading() },
            onSuccess = { activeTestId ->
                if (activeTestId.isEmpty() || activeTestId == currentTestId) {
                    if (activeTestId.isEmpty()) {
                        testSeriesViewModel.clearAnswersForCurrentTest(currentTestId)
                    }
                    NavigationHelper.navigateToTestScreen(requireActivity(), currentTestId!!)
                } else {
                    Toast.makeText(requireContext(), "Test already in progress", Toast.LENGTH_SHORT).show()
                }
            },
            onError = { errorMsg -> showError(errorMsg) }
        )

    }

    private fun showError(string: String) {
        Toast.makeText(requireContext(), string, Toast.LENGTH_SHORT).show()
    }

    private fun showTestItems(items: List<TestItem>) {
        originalItems = items
        showTestItemUI(items)
    }

    private fun showLoading() {

    }

    private fun showTestItemUI(items: List<TestItem>) {
        binding.testListRecycler.adapter = TestListAdapter(items, this)
    }

    fun filterData(query: String) {
        val filtered = originalItems.filter {
            it.name.contains(query, ignoreCase = true)
        }
        showTestItems(filtered)
    }

    override fun onClick(testItem: TestItem) {
        currentTestId = testItem.id
        testSeriesViewModel.getActiveTest()
//        NavigationHelper.navigateToTestScreen(requireActivity(), testItem.id)
    }

    companion object {
        @JvmStatic
        fun newInstance(filter: String, testType: String) =
            TestListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_FILTER, filter)
                    putString(ARG_TEST_TYPE, testType)
                }
            }
    }
}
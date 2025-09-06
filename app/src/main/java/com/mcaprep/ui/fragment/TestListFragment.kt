package com.mcaprep.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.mcaprep.R
import com.mcaprep.databinding.FragmentTestListBinding
import com.mcaprep.domain.model.TestItem
import com.mcaprep.ui.adapter.OnClickListeners
import com.mcaprep.ui.adapter.TestListAdapter
import com.mcaprep.ui.viewmodel.TestSeriesViewModel
import com.mcaprep.utils.NavigationHelper
import com.mcaprep.utils.extentions.observeResource
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_FILTER = "filter"
private const val ARG_TEST_SERIES_NAME = "testSeriesName"

/**
 * A simple [Fragment] subclass.
 * Use the [TestListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class TestListFragment : Fragment(), OnClickListeners {
    // TODO: Rename and change types of parameters
    private var filter: String? = null
    private var testSeriesName: String? = null
    private var _binding: FragmentTestListBinding? = null
    private val binding get() = _binding!!
    private val testSeriesViewModel: TestSeriesViewModel by viewModels()
    private var originalItems: List<TestItem> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            filter = it.getString(ARG_FILTER)
            testSeriesName = it.getString(ARG_TEST_SERIES_NAME)
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

        val type = when (filter) {
            "Mocks" -> "MOCK"
            "PYQ" -> "PYQ"
            else -> ""
        }
        testSeriesViewModel.getTestSeries(type)

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
        NavigationHelper.navigateToTestScreen(requireActivity(), testItem.id)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TestListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(filter: String, testSeriesName: String) =
            TestListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_FILTER, filter)
                    putString(ARG_TEST_SERIES_NAME, testSeriesName)
                }
            }
    }
}
package com.mcaprep.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcaprep.data.remote.model.EndTestRequest
import com.mcaprep.data.repository.TestSeriesRepository
import com.mcaprep.domain.mapper.toDomain
import com.mcaprep.domain.mapper.toDto
import com.mcaprep.domain.model.Solutions
import com.mcaprep.domain.model.TestDetails
import com.mcaprep.domain.model.TestItem
import com.mcaprep.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestSeriesViewModel @Inject constructor(
    private val testSeriesRepository: TestSeriesRepository
) : ViewModel() {

    private val _testSeries = MutableLiveData<Resource<List<TestItem>>>()
    val testSeries: LiveData<Resource<List<TestItem>>> = _testSeries
    val searchQuery = MutableLiveData<String>()

    private val _testDetails = MutableLiveData<Resource<TestDetails>>()
    val testDetails: LiveData<Resource<TestDetails>> = _testDetails

    private val _endTest = MutableLiveData<Resource<Boolean>>()
    val endTest: LiveData<Resource<Boolean>> = _endTest

    fun getTestSeries(type: String) {
        viewModelScope.launch {
            _testSeries.value = Resource.Loading
            try {
                val response = testSeriesRepository.getTestSeries(type)
                val testItems = response.res.data.map { it.toDomain() }
                _testSeries.value = Resource.Success(testItems)
            } catch (e: Exception) {
                _testSeries.value = Resource.Failure(e.message ?: "Unknown error")
            }
        }
    }

    fun startExistingTest(testId: String) {
        viewModelScope.launch {
            _testSeries.value = Resource.Loading
            try {
                val response = testSeriesRepository.startExistingTest(testId)
                val testDetails = response.res.toDomain()
                _testDetails.value = Resource.Success(testDetails)
            } catch (e: Exception) {
                _testSeries.value = Resource.Failure(e.message ?: "Unknown error")
            }
        }
    }

    fun endTest(testId: String, solution: List<Solutions>) {
        viewModelScope.launch {
            _endTest.value = Resource.Loading
            try {
                val endTestRequest = EndTestRequest(testId, solution.map { it.toDto() })
                val response = testSeriesRepository.endTest(endTestRequest)
                _endTest.value = Resource.Success(response)
            } catch (e: Exception) {
                _endTest.value = Resource.Failure(e.message ?: "Unknown error")
            }
        }
    }

}
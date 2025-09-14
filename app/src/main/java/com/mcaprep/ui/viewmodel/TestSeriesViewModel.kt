package com.mcaprep.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcaprep.data.local.dao.AnswerDao
import com.mcaprep.data.local.entities.AnswerEntity
import com.mcaprep.data.remote.model.EndTestRequest
import com.mcaprep.data.repository.TestSeriesRepository
import com.mcaprep.domain.mapper.toDomain
import com.mcaprep.domain.mapper.toDto
import com.mcaprep.domain.model.ResultWithQuestion
import com.mcaprep.domain.model.TestDetails
import com.mcaprep.domain.model.TestItem
import com.mcaprep.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestSeriesViewModel @Inject constructor(
    private val testSeriesRepository: TestSeriesRepository,
    private val answerDao: AnswerDao
) : ViewModel() {

    private val _testSeries = MutableLiveData<Resource<List<TestItem>>>()
    val testSeries: LiveData<Resource<List<TestItem>>> = _testSeries
    val searchQuery = MutableLiveData<String>()

    private val _testDetails = MutableLiveData<Resource<TestDetails>>()
    val testDetails: LiveData<Resource<TestDetails>> = _testDetails

    private val _endTest = MutableLiveData<Resource<ResultWithQuestion>>()
    val endTest: LiveData<Resource<ResultWithQuestion>> = _endTest

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
            _testDetails.value = Resource.Loading
            try {
                val response = testSeriesRepository.startExistingTest(testId)
                val testDetails = response.res.toDomain()
                _testDetails.value = Resource.Success(testDetails)
            } catch (e: Exception) {
                _testDetails.value = Resource.Failure(e.message ?: "Unknown error")
            }
        }
    }

    fun endTest() {
        viewModelScope.launch {
            _endTest.value = Resource.Loading
            val testId = currentTestId ?: return@launch
            Log.e("Submit", "endTest: $testId")
            val selectedOption = getAllSelectedAnswersForCurrentTest()
            try {
                val endTestRequest = EndTestRequest(testId, selectedOption.map { it.toDto() })
                val response = testSeriesRepository.endTest(endTestRequest)
                _endTest.value = Resource.Success(response.toDomain())
            } catch (e: Exception) {
                _endTest.value = Resource.Failure(e.message ?: "Unknown error")
            }
        }
    }

    // Holds the current test ID, set when a test starts
    var currentTestId: String? = null
        private set // Make setter private if it's set internally

    fun initializeTest(testId: String) {
        currentTestId = testId
        // Any other test initialization logic
    }

    fun saveSelectedAnswer(questionId: String, selectedOptionKey: String) {
        viewModelScope.launch {
            currentTestId?.let { testId ->
                val answer = AnswerEntity(
                    testId = testId,
                    questionId = questionId,
                    selectedOption = selectedOptionKey,
                    markForReview = false
                )
                answerDao.insertOrUpdateAnswer(answer)
            }
        }
    }

    suspend fun getSelectedAnswerForQuestion(questionId: String): String? {
        return currentTestId?.let { testId ->
            answerDao.getAnswerForQuestion(testId, questionId)?.selectedOption
        }
    }

    // To be used by TestScreenActivity for submitting answers
    suspend fun getAllSelectedAnswersForCurrentTest(): List<AnswerEntity> {
        return currentTestId?.let { testId ->
            answerDao.getAnswersForTest(testId).firstOrNull() ?: emptyList()
        } ?: emptyList()
    }

    // Optional: To clear answers from DB after successful submission
    fun clearAnswersForCurrentTest() {
        viewModelScope.launch {
            currentTestId?.let { testId ->
                answerDao.clearAnswersForTest(testId)
            }
        }
    }
}
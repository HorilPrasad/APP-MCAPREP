package com.mcaprep.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.mcaprep.data.local.dao.AnswerDao
import com.mcaprep.data.local.entities.AnswerEntity
import com.mcaprep.data.remote.model.EndTestRequest
import com.mcaprep.data.repository.TestSeriesRepository
import com.mcaprep.domain.mapper.toDomain
import com.mcaprep.domain.mapper.toDto
import com.mcaprep.domain.model.AnsweredOption
import com.mcaprep.domain.model.ResultWithQuestion
import com.mcaprep.domain.model.TestDetails
import com.mcaprep.domain.model.TestHistory
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

    private val _activeTest = MutableLiveData<Resource<String>>()
    val activeTest: LiveData<Resource<String>> = _activeTest

    val selectedOption: MutableLiveData<AnsweredOption> = MutableLiveData()

    // 1. The private MediatorLiveData that will be updated.
    private val _answers = MediatorLiveData<List<AnswerEntity>>()

    // 2. The public, unchangeable LiveData the UI will observe.
    val answers: LiveData<List<AnswerEntity>> = _answers

    // 3. Keep track of the current LiveData source from the DAO.
    private var currentSource: LiveData<List<AnswerEntity>>? = null

    private val _testHistory = MutableLiveData<Resource<TestHistory>>()
    val testHistory: LiveData<Resource<TestHistory>> = _testHistory


    fun getTestSeries(type: String) {
        if (type.contains("SW")) {
            getNimcetSeries(type)
        } else {
            if (type.contains("MOCK"))
                getMockOthers(type)
            else
                getPyqOthers(type)
        }
    }

    fun getNimcetSeries(type: String) {
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

    fun getMockOthers(type: String) {
        viewModelScope.launch {
            _testSeries.value = Resource.Loading
            try {
                val response = testSeriesRepository.getMockOthers()
                val testItem = response.res.find { it.id == type }?.tests?.map { it.toDomain() }
                if (!testItem.isNullOrEmpty())
                    _testSeries.value = Resource.Success(testItem)
                else
                    _testSeries.value = Resource.Failure("No test found")
            } catch (e: Exception) {
                _testSeries.value = Resource.Failure(e.message ?: "Unknown error")
            }
        }
    }

    fun getPyqOthers(type: String) {
        viewModelScope.launch {
            _testSeries.value = Resource.Loading
            try {
                val response = testSeriesRepository.getPyqOthers()
                val testItem = response.res.find { it.id == type }?.tests?.map { it.toDomain() }
                if (!testItem.isNullOrEmpty())
                    _testSeries.value = Resource.Success(testItem)
                else
                    _testSeries.value = Resource.Failure("No test found")
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
            val selectedOption = answers.value ?: emptyList()
            try {
                val endTestRequest = EndTestRequest(testId, selectedOption.map { it.toDto() })
                val response = testSeriesRepository.endTest(endTestRequest)
                _endTest.value = Resource.Success(response.toDomain())
            } catch (e: Exception) {
                _endTest.value = Resource.Failure(e.message ?: "Unknown error")
            }
        }
    }

    fun getActiveTest() {
        viewModelScope.launch {
            _activeTest.value = Resource.Loading
            try {
                val response = testSeriesRepository.getActiveTest()
                _activeTest.value = Resource.Success(response)
                Log.e("getActiveTest", "getActiveTest: $response" )
            } catch (e: Exception) {
                _activeTest.value = Resource.Failure(e.message ?: "Unknown error")
            }
        }
    }

    fun getTestHistory(id: String, count: Int) {
        viewModelScope.launch {
            _testHistory.value = Resource.Loading
            try {
                val response = testSeriesRepository.getTestHistory(id, count)
                _testHistory.value = Resource.Success(response.success.toDomain())
            } catch (e: Exception) {
                _testHistory.value = Resource.Failure(e.message ?: "Unknown error")
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
    fun markForReviewAnswer(questionId: String, markForReview: Boolean) {
        viewModelScope.launch {
            currentTestId?.let { testId ->
                val answer = AnswerEntity(
                    testId = testId,
                    questionId = questionId,
                    selectedOption = "",
                    markForReview = markForReview
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
//    suspend fun getAllSelectedAnswersForCurrentTest(): List<AnswerEntity> {
//        return currentTestId?.let { testId ->
//            answerDao.getAnswersForTest(testId).firstOrNull() ?: emptyList()
//        } ?: emptyList()
//    }

    // 4. The UI calls this function once to start observing.
    fun loadAnswersForTest(testId: String) {
        // If we are already observing a source, remove it first.
        currentSource?.let { _answers.removeSource(it) }

        // Get the new LiveData source from the DAO.
        val newSource = answerDao.getAnswersForTest(testId)

        // Tell our MediatorLiveData to listen to this new source.
        _answers.addSource(newSource) { updatedList ->
            // When the DAO's LiveData emits a new list,
            // we post that new list to our own _answers LiveData.
            _answers.value = updatedList
        }

        // Update the current source reference.
        currentSource = newSource
    }

    // Optional: To clear answers from DB after successful submission
    fun clearAnswersForCurrentTest(testId: String? = currentTestId) {
        viewModelScope.launch {
            testId?.let { id ->
                answerDao.clearAnswersForTest(id)
            }
        }
    }
}
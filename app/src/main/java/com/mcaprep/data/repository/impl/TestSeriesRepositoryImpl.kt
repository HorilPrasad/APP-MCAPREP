package com.mcaprep.data.repository.impl

import com.mcaprep.data.remote.api.ApiService
import com.mcaprep.data.remote.model.EndTestRequest
import com.mcaprep.data.remote.model.StartExistingTestRequest
import com.mcaprep.data.remote.model.StartExistingTestResponse
import com.mcaprep.data.remote.model.TestSeriesRequest
import com.mcaprep.data.remote.model.TestSeriesResponse
import com.mcaprep.data.repository.TestSeriesRepository
import javax.inject.Inject

class TestSeriesRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : TestSeriesRepository {

    override suspend fun getTestSeries(type: String): TestSeriesResponse {
        try {
            val response = apiService.getTestSeries(TestSeriesRequest(type))
            if (response.isSuccessful) {
                return response.body() ?: throw Exception("Response body is null")
            } else {
                throw Exception("Server error: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun startExistingTest(testId: String): StartExistingTestResponse {
        try {
            val response = apiService.startExistingTest(StartExistingTestRequest(testId))
            if (response.isSuccessful) {
                return response.body() ?: throw Exception("Response body is null")
            } else {
                throw Exception("Server error: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun endTest(endTestRequest: EndTestRequest): Boolean {
        try {
            val response = apiService.endTest(endTestRequest)
            if (response.isSuccessful) {
                return true
            } else {
                throw Exception("Server error: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }

}
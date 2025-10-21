package com.mcaprep.data.repository.impl

import android.util.Log
import com.mcaprep.data.remote.api.ApiService
import com.mcaprep.data.remote.model.EndTestRequest
import com.mcaprep.data.remote.model.EndTestResponse
import com.mcaprep.data.remote.model.StartExistingTestRequest
import com.mcaprep.data.remote.model.StartExistingTestResponse
import com.mcaprep.data.remote.model.TestHistoryResponse
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

    override suspend fun endTest(endTestRequest: EndTestRequest): EndTestResponse {
        try {
            val response = apiService.endTest(endTestRequest)
            if (response.isSuccessful) {
                return response.body() ?: throw Exception("Response body is null")
            } else {
                throw Exception("Server error: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getActiveTest(): String {
        try {
            val response = apiService.getActiveTest()
            if (response.isSuccessful) {
                val activeTestId  = response.body()?.res?.id
                if (activeTestId != null) {
                    return activeTestId
                } else {
                    return ""
                }
            } else {
                throw Exception("Server error: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getTestHistory(
        id: String,
        count: Int
    ): TestHistoryResponse {
        try {
            val response = apiService.getTestHistory(id, count)
            if (response.isSuccessful) {
                return response.body() ?: throw Exception("Response body is null")
            } else {
                throw Exception("Server error: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }

}
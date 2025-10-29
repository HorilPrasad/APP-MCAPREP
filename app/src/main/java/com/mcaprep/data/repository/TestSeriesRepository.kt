package com.mcaprep.data.repository

import com.mcaprep.data.remote.model.EndTestRequest
import com.mcaprep.data.remote.model.EndTestResponse
import com.mcaprep.data.remote.model.OtherTestSeriesResponse
import com.mcaprep.data.remote.model.StartExistingTestResponse
import com.mcaprep.data.remote.model.TestHistoryResponse
import com.mcaprep.data.remote.model.TestSeriesResponse

interface TestSeriesRepository {
    suspend fun getTestSeries(type: String): TestSeriesResponse
    suspend fun getSectionWiseTestSeries(type: String): TestSeriesResponse

    suspend fun startExistingTest(testId: String): StartExistingTestResponse

    suspend fun endTest(endTestRequest: EndTestRequest): EndTestResponse

    suspend fun getActiveTest(): String

    suspend fun getTestHistory(id: String, count: Int): TestHistoryResponse

    suspend fun getMockOthers(): OtherTestSeriesResponse

    suspend fun getPyqOthers(): OtherTestSeriesResponse
}
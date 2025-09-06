package com.mcaprep.data.repository

import com.mcaprep.data.remote.model.EndTestRequest
import com.mcaprep.data.remote.model.StartExistingTestResponse
import com.mcaprep.data.remote.model.TestSeriesResponse

interface TestSeriesRepository {
    suspend fun getTestSeries(type: String): TestSeriesResponse

    suspend fun startExistingTest(testId: String): StartExistingTestResponse

    suspend fun endTest(endTestRequest: EndTestRequest): Boolean

}
package com.mcaprep.data.remote.api

import com.mcaprep.data.remote.model.EndTestRequest
import com.mcaprep.data.remote.model.EndTestResponse
import com.mcaprep.data.remote.model.LoginRequest
import com.mcaprep.data.remote.model.LoginResponse
import com.mcaprep.data.remote.model.NotificationImageResponse
import com.mcaprep.data.remote.model.StartExistingTestRequest
import com.mcaprep.data.remote.model.StartExistingTestResponse
import com.mcaprep.data.remote.model.TestHistoryResponse
import com.mcaprep.data.remote.model.TestSeriesRequest
import com.mcaprep.data.remote.model.TestSeriesResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("notificationImage")
    suspend fun notificationImage(): Response<NotificationImageResponse>

    @POST("series")
    suspend fun getTestSeries(@Body testSeriesRequest: TestSeriesRequest): Response<TestSeriesResponse>

    @POST("test/start-existing-test")
    suspend fun startExistingTest(@Body startExistingTestRequest: StartExistingTestRequest): Response<StartExistingTestResponse>

    @POST("test/end-test")
    suspend fun endTest(@Body endTestRequest: EndTestRequest): Response<EndTestResponse>

    @GET("test/active-test")
    suspend fun getActiveTest(): Response<StartExistingTestResponse>

    @GET("test-history/{id}/count/{count}")
    suspend fun getTestHistory(@Path("id") id: String, @Path("count") count: Int): Response<TestHistoryResponse>
}

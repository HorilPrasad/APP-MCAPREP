package com.mcaprep.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StartExistingTestRequest(
    @SerialName("testId")
    val testId: String
)
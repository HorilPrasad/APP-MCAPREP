package com.mcaprep.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TestSeriesRequest(
    @SerialName("type")
    val type: String
)
package com.mcaprep.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OtherTestSeriesResponse(
    @SerialName("Success")
    val res: List<OtherTestSeriesDto>
)

@Serializable
data class OtherTestSeriesDto(
    @SerialName("_id")
    val id: String,
    @SerialName("name")
    val name: List<String>,
    @SerialName("description")
    val description: List<String>,
    @SerialName("count")
    val count: Int,
    @SerialName("tests")
    val tests: List<TestItemDto>,
)

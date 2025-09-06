package com.mcaprep.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationImageResponse(
   @SerialName("Success")
    val res: List<NotificationItemDto>
)

@Serializable
data class NotificationItemDto(
    @SerialName("_id")
    val id: String,
    @SerialName("image")
    val imageUrl: String,
    @SerialName("link")
    val link: String
)

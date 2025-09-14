package com.mcaprep.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Solutions(
    val question: String,
    val option: String?
): Parcelable

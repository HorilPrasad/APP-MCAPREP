package com.mcaprep.utils

sealed class Resource<out T> {
    // Loading state (network call is in progress)
    data object Loading : Resource<Nothing>()

    // Success state (network call succeeded)
    data class Success<out T>(val data: T) : Resource<T>()

    // Failure state (network call failed)
    data class Failure(val message: String, val throwable: Throwable? = null) : Resource<Nothing>()
}
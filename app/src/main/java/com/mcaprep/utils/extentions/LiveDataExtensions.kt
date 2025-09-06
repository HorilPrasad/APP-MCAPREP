package com.mcaprep.utils.extentions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.mcaprep.utils.Resource

inline fun <T> LiveData<Resource<T>>.observeResource(
    owner: LifecycleOwner,
    crossinline onLoading: () -> Unit = {},
    crossinline onSuccess: (T) -> Unit = {},
    crossinline onError: (String) -> Unit = {}
) {
    observe(owner) { resource ->
        when (resource) {
            is Resource.Loading -> onLoading()
            is Resource.Success -> onSuccess(resource.data)
            is Resource.Failure -> onError(resource.message)
        }
    }
}

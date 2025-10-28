package com.mcaprep.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcaprep.data.local.PrefManager
import com.mcaprep.data.repository.UserRepository
import com.mcaprep.domain.mapper.toDomain
import com.mcaprep.domain.model.User
import com.mcaprep.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val pref: PrefManager
): ViewModel() {

    private val _user = MutableLiveData<Resource<User>>()
    val user: LiveData<Resource<User>> = _user

    fun getCurrentUser() {
        viewModelScope.launch {
            _user.value = Resource.Loading
            try {
                val id = pref.getUserId()
                val user = userRepository.getUser(id)
                _user.value = Resource.Success(user.toDomain())
            } catch (e: Exception) {
                _user.value = Resource.Failure(e.message.toString())
            }
        }
    }
}
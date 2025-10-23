package com.mcaprep.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcaprep.data.local.PrefManager
import com.mcaprep.data.remote.model.LoginRequest
import com.mcaprep.data.repository.AuthRepository
import com.mcaprep.domain.mapper.toDomain
import com.mcaprep.domain.model.User
import com.mcaprep.ui.adapter.CarouselItem
import com.mcaprep.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val pref: PrefManager
): ViewModel() {

    private val _loginResponse = MutableLiveData<Resource<User>>()
    val loginResponse: LiveData<Resource<User>> = _loginResponse

    private val _notificationImageResponse = MutableLiveData<Resource<List<CarouselItem>>>()
    val notificationImageResponse: LiveData<Resource<List<CarouselItem>>> = _notificationImageResponse

    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            _loginResponse.value = Resource.Loading

            try {
                val response = authRepository.login(loginRequest)
                _loginResponse.value = Resource.Success(response.user.toDomain())
                pref.setAuthToken(response.token)
            } catch (e: Exception) {
                _loginResponse.value = Resource.Failure(e.message ?: "Unknown error")
            }

        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    fun notificationImage() {
        viewModelScope.launch {
            _notificationImageResponse.value = Resource.Loading
            try {
                val response = authRepository.notificationImage()
                val carouselItems = response.res.map {
                    CarouselItem(it.imageUrl, it.link)
                }
                _notificationImageResponse.value = Resource.Success(carouselItems)
            } catch (e: Exception) {
                _notificationImageResponse.value = Resource.Failure(e.message ?: "Unknown error")
            }
        }
    }

    fun setLoggedIn(username: String, userId: String) {
        viewModelScope.launch {
            pref.setLoggedIn(true)
            pref.setUsername(username)
            pref.setUserId(userId)
        }
    }

}
package com.dicoding.sahabatgula.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.sahabatgula.data.local.entity.UserProfile
import com.dicoding.sahabatgula.repository.UserProfileRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: UserProfileRepository): ViewModel() {

    private val _loginResult = MutableLiveData<Result<UserProfile>>()
    val loginResult:LiveData<Result<UserProfile>> = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = loginRepository.login(email, password)
            _loginResult.value = result
        }
    }

}
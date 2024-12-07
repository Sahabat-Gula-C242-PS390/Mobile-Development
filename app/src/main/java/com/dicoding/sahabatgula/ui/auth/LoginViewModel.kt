package com.dicoding.sahabatgula.ui.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.sahabatgula.repository.LoginRepository

class LoginViewModel(private val loginRepository: LoginRepository): ViewModel() {

    private val _userId = MutableLiveData<Long>()
}
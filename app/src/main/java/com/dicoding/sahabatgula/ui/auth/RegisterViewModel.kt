package com.dicoding.sahabatgula.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.sahabatgula.data.local.entity.UserProfile
import com.dicoding.sahabatgula.repository.UserProfileRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val userProfileRepository: UserProfileRepository): ViewModel() {

    // menyimpan sementara data user
    private val _userProfile = MutableLiveData<UserProfile>()
    val userProfile: LiveData<UserProfile> = _userProfile

    // menyimpan data di live data
    fun saveUserProfile(userProfile: UserProfile) {
        _userProfile.value = userProfile
    }

    // menyimpan data ke room database
    fun saveToDatabase(userProfile: UserProfile) {
        viewModelScope.launch {
            userProfileRepository.insertUserProfile(userProfile)
        }
    }

    // cek apakah email sudah terdaftar
    suspend fun isEmailRegistered(email: String): Boolean {
        val userProfile = userProfileRepository.getUserProfileByEmail(email)
        return userProfile!=null
    }
}
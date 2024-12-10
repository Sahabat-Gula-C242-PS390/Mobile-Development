package com.dicoding.sahabatgula.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.sahabatgula.data.local.room.UserProfileDao

class ProfileViewModelFactory(private val userProfileDao: UserProfileDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(userProfileDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
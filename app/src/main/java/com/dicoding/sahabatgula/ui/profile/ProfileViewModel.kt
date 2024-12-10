package com.dicoding.sahabatgula.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.sahabatgula.data.local.entity.UserProfile
import com.dicoding.sahabatgula.data.local.room.UserProfileDao
import kotlinx.coroutines.launch

class ProfileViewModel(private val userProfileDao: UserProfileDao) : ViewModel() {

    private val _dataUser = MutableLiveData<UserProfile>()
    val dataUser: LiveData<UserProfile> = _dataUser

    // Fetch data from database berdasarkan userId
    fun fetchUserProfile(userId: String?) {
        viewModelScope.launch {
            // Ambil data userProfile berdasarkan userId
            val userProfile = userProfileDao.getUserProfile(userId) // Sesuaikan dengan fungsi DAO Anda
            if (userProfile != null) {
                _dataUser.value = userProfile
            }
        }
    }
}
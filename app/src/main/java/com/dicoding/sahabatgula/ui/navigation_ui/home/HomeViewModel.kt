package com.dicoding.sahabatgula.ui.navigation_ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.sahabatgula.data.local.entity.UserProfile
import com.dicoding.sahabatgula.repository.UserProfileRepository
import kotlinx.coroutines.launch

class HomeViewModel (private val userProfileRepository: UserProfileRepository) : ViewModel() {

    private val _dataUser = MutableLiveData<UserProfile>()
    val dataUser: LiveData<UserProfile> = _dataUser

    // fetch data from API
    fun fetchUserData(userId: String) {
        viewModelScope.launch {
            val result = userProfileRepository.getUserProfileFromApi(userId)
            result.onSuccess { userProfile ->
                _dataUser.postValue(userProfile)
            }
            result.onFailure {
                Log.e("HomeViewModel", "Error fetching user data from API")
            }
        }
    }

}
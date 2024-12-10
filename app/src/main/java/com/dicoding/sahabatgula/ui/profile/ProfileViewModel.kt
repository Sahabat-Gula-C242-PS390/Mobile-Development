package com.dicoding.sahabatgula.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.sahabatgula.data.local.entity.UserProfile
import com.dicoding.sahabatgula.repository.UserProfileRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val userProfileRepository: UserProfileRepository) : ViewModel() {
    private val _dataUser = MutableLiveData<UserProfile>()
    val dataUser: LiveData<UserProfile> = _dataUser

    // fetch data from API
    fun fetchUserProfile(userId: String?) {
        viewModelScope.launch {

            val result = userProfileRepository.getUserProfileFromApi(userId)
            result.onSuccess { userProfile ->
                _dataUser.postValue(userProfile)
            }
            result.onFailure {
                Log.e("ProfileViewModel", "Error fetching user profile from API", it)
            }
        }
    }
}
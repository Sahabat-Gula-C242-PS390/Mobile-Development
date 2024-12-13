package com.dicoding.sahabatgula.ui.navigation_ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.sahabatgula.data.local.entity.UserProfile
import com.dicoding.sahabatgula.helper.ScanData
import com.dicoding.sahabatgula.repository.UserProfileRepository
import kotlinx.coroutines.launch

class HomeViewModel (private val userProfileRepository: UserProfileRepository) : ViewModel() {

    private val _dataUser = MutableLiveData<UserProfile>()
    val dataUser: LiveData<UserProfile> = _dataUser

    // LiveData for ScanData List
    private val _scanDataList = MutableLiveData<List<ScanData>>()
    val scanDataList: LiveData<List<ScanData>> = _scanDataList

    // Mutable data for totals
    private val _totals = MutableLiveData<Map<String, Double>>()
    val totals: LiveData<Map<String, Double>> = _totals

    init {
        // Initialize scan data list as an empty list
        _scanDataList.value = emptyList()
    }

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

    // Add ScanData to the list
    fun addScanData(scanData: ScanData) {
        val currentList = _scanDataList.value.orEmpty().toMutableList()
        currentList.add(scanData)
        _scanDataList.value = currentList
        calculateTotals(currentList)
    }

    // Clear all ScanData
    fun clearScanData() {
        _scanDataList.value = emptyList()
        _totals.value = emptyMap()
    }

    // Calculate totals for the ScanData list
    private fun calculateTotals(scanDataList: List<ScanData>) {
        val totalKarbo = scanDataList.sumOf { it.karbo.toDouble() }
        val totalLemak = scanDataList.sumOf { it.lemak }
        val totalGula = scanDataList.sumOf { it.gula.toDouble() }
        val totalProtein = scanDataList.sumOf { it.protein.toDouble() }
        val totalKalori = scanDataList.sumOf { it.totalKalori }

        _totals.value = mapOf(
            "totalKarbo" to totalKarbo,
            "totalLemak" to totalLemak,
            "totalGula" to totalGula,
            "totalProtein" to totalProtein,
            "totalKalori" to totalKalori
        )
    }

}
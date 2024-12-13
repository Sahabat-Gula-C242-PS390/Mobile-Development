package com.dicoding.sahabatgula.ui.auth

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.sahabatgula.data.local.entity.UserProfile
import com.dicoding.sahabatgula.data.remote.response.UserProfileResponse
import com.dicoding.sahabatgula.repository.UserProfileRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val userProfileRepository: UserProfileRepository): ViewModel() {

    // menyimpan sementara data user
    private val _userProfile = MutableLiveData<UserProfile>()
    val userProfile: LiveData<UserProfile> = _userProfile

    // mengupdate data sementara
    fun updateUserProfile(partialProfile: UserProfile) {
        Log.d("RegisterViewModel", "Updating user profile with data: $partialProfile")
        val currentProfile = _userProfile.value ?: UserProfile(id = partialProfile.id)
        _userProfile.value = currentProfile.copy(
            id = partialProfile.id,
            name = partialProfile.name.takeIf { it!!.isNotBlank() } ?: currentProfile.name,
            email = partialProfile.email.takeIf { it!!.isNotBlank() } ?: currentProfile.email,
            password = partialProfile.password.takeIf { it!!.isNotBlank() } ?: currentProfile.password,
            gender = partialProfile.gender.takeIf { it!!.isNotBlank() } ?: currentProfile.gender,
            umur = partialProfile.umur.takeIf { it != 0 } ?: currentProfile.umur,
            berat = partialProfile.berat.takeIf { it != 0 } ?: currentProfile.berat,
            tinggi = partialProfile.tinggi.takeIf { it != 0 } ?: currentProfile.tinggi,
            lingkarPinggang = partialProfile.lingkarPinggang.takeIf { it!!.isNotBlank() } ?: currentProfile.lingkarPinggang,
            riwayatDiabetes = partialProfile.riwayatDiabetes.takeIf { it != 0 } ?: currentProfile.riwayatDiabetes,
            tekananDarahTinggi = partialProfile.tekananDarahTinggi.takeIf { it != 0 } ?: currentProfile.tekananDarahTinggi,
            gulaDarahTinggi = partialProfile.gulaDarahTinggi.takeIf { it != 0 } ?: currentProfile.gulaDarahTinggi,
            tingkatAktivitas = partialProfile.tingkatAktivitas.takeIf { it!!.isNotBlank() } ?: currentProfile.tingkatAktivitas,
            konsumsiBuah = partialProfile.konsumsiBuah.takeIf { it != 0 } ?: currentProfile.konsumsiBuah,
            gulaHarian = partialProfile.gulaHarian.takeIf { it != 0 } ?: currentProfile.gulaHarian,
            kaloriHarian = partialProfile.kaloriHarian.takeIf { it != 0 } ?: currentProfile.kaloriHarian,
            karbohidratHarian = partialProfile.karbohidratHarian.takeIf { it != 0 } ?: currentProfile.karbohidratHarian,
            lemakHarian = partialProfile.lemakHarian.takeIf { it != 0 } ?: currentProfile.lemakHarian,
            proteinHarian = partialProfile.proteinHarian.takeIf { it != 0 } ?: currentProfile.proteinHarian
        )
    }

    // menyimpan data ke lokal
    fun saveToDatabase(userProfile: UserProfile) {
        viewModelScope.launch {
            _userProfile.value?.let{
                userProfileRepository.insertUserProfile(userProfile)
            }
        }
    }

    // cek apakah email sudah terdaftar
    suspend fun isEmailRegistered(email: String): Boolean {
        val userProfile = userProfileRepository.getUserProfileByEmail(email)
        return userProfile!=null
    }

    fun registerUserProfileToRemote(userProfile: UserProfile, callback: (UserProfileResponse?) -> Unit) {
        userProfileRepository.registerUserProfileToRemote(userProfile, callback)
    }

    fun getUserIdFromPreferences(context: Context): String? {
        return userProfileRepository.getUserIdFromPreferences(context)
    }

    // ViewModel id
    private val _userId = MutableLiveData<Long>()
    val  userId: LiveData<Long> = _userId

    fun setUserId(userId: Long) {
        _userId.value = userId
    }

}
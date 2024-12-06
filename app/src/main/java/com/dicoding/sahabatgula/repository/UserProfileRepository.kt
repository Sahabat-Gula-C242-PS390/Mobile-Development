package com.dicoding.sahabatgula.repository

import androidx.lifecycle.MediatorLiveData
import com.dicoding.sahabatgula.data.local.entity.UserProfile
import com.dicoding.sahabatgula.data.local.room.UserProfileDao
import com.dicoding.sahabatgula.data.remote.response.ListUserProfileItem
import com.dicoding.sahabatgula.data.remote.response.UserProfileResponse
import com.dicoding.sahabatgula.data.remote.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserProfileRepository (private val userProfileDao: UserProfileDao, private val apiService: ApiService) {

    // di bawah ini berkaitan dengan database lokal, tujuannya untuk akses offline

    // mengambil data profil pengguna berdasarkan id
    suspend fun getUserProfile(id: Long): UserProfile {
        return userProfileDao.getUserProfile(id)
    }

    // mengambil semua data pengguna
    suspend fun getAllUserProfile(): List<UserProfile> {
        return userProfileDao.getAllUserProfile()
    }

    // menyimpan data profil pengguna
    /*fungsi ini tidak menggunakan List<UserProfile>  karena bertujuan untuk menyimpan satu profile penggguna
    atau single insert, bukan banyak data dari pengguna atau batch insert*/
    suspend fun insertUserProfile(userProfile: UserProfile) {
        userProfileDao.insertUserProfile(userProfile)
    }

    // memperbarui data profil pengguna
    suspend fun updateUserProfile(userProfile: UserProfile) {
        userProfileDao.updateUserProfile(userProfile)
    }

    // menghapus semua data profil pengguna
    suspend fun deleteAllUserProfile() {
        userProfileDao.deleteAllUserProfile()
    }

    private val result = MediatorLiveData<Result<List<UserProfile>>>()
    
    // menyimpan data user ke API (remote)
    /*karena register itu methodnya post, maka di repository ini dibuat sebuah method untuk mengirim data dari lokal
    ke network (API), misalnya setelah menambahkan atau memperbarui data. di sini juag menangani response api*/
    fun registerUserProfileToRemote(userProfile: UserProfile, onResponse:(UserProfileResponse?)-> Unit) {
        // result.value = Result.Loading
        val userProfileItem = ListUserProfileItem(
            name = userProfile.name,
            email = userProfile.email,
            password = userProfile.password
//            gender = userProfile.gender,
//            umur = userProfile.umur,
//            berat = userProfile.berat,
//            tinggi = userProfile.tinggi,
//            lingkarPinggang = userProfile.lingkarPinggang,
//            tekananDarahTinggi = userProfile.tekananDarahTinggi,
//            gulaDarahTinggi = userProfile.gulaDarahTinggi,
//            tingkatAktivitas = userProfile.tingkatAktivitas,
//            konsumsiBuah = userProfile.konsumsiBuah,
//            gulaHarian = userProfile.gulaHarian,
//            kaloriHarian = userProfile.kaloriHarian,
//            karbohidratHarian = userProfile.karbohidratHarian,
//            lemakHarian = userProfile.lemakHarian,
//            proteinHarian = userProfile.proteinHarian
        )

        apiService.register(userProfileItem).enqueue(object:Callback<UserProfileResponse>{
            override fun onResponse(
                call: Call<UserProfileResponse>,
                response: Response<UserProfileResponse>
            ) {
                if (response.isSuccessful) {
                    onResponse(response.body())
                }
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                onResponse(null)
            }
        })

    }

    // cek apakah email sudah terdaftar atau belum
    suspend fun getUserProfileByEmail(email: String): UserProfile {
        return userProfileDao.getUserProfileByEmail(email)
    }

    companion object {
        @Volatile
        private var instance: UserProfileRepository? = null
        fun getInstance(
            userProfileDao: UserProfileDao,
            apiService: ApiService): UserProfileRepository =
            instance ?: synchronized(this) {
                instance ?: UserProfileRepository(userProfileDao, apiService)
            }.also { instance = it}

    }

}
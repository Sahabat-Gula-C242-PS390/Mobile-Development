package com.dicoding.sahabatgula.repository

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.dicoding.sahabatgula.data.local.entity.UserProfile
import com.dicoding.sahabatgula.data.local.room.UserProfileDao
import com.dicoding.sahabatgula.data.remote.response.ListUserProfileItem
import com.dicoding.sahabatgula.data.remote.response.UserProfileResponse
import com.dicoding.sahabatgula.data.remote.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserProfileRepository (private val userProfileDao: UserProfileDao, private val apiService: ApiService, private val context: Context) {


    // di bawah ini berkaitan dengan database lokal, tujuannya untuk akses offline

    // menyimpan data profil pengguna
    /*fungsi ini tidak menggunakan List<UserProfile>  karena bertujuan untuk menyimpan satu profile penggguna
    atau single insert, bukan banyak data dari pengguna atau batch insert*/
    suspend fun insertUserProfile(userProfile: UserProfile) {
        userProfileDao.insertUserProfile(userProfile)
    }

    // Mendapatkan SharedPreferences menggunakan Application context
    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.applicationContext.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
    }

    // Fungsi untuk menyimpan userId ke SharedPreferences
    fun saveUserIdToPreferences(context: Context, userId: String) {
        val sharedPreferences = getSharedPreferences(context)
        sharedPreferences.edit().apply {
            putString("userId", userId)
            apply()
        }
    }

    // Fungsi untuk mengambil userId dari SharedPreferences
    fun getUserIdFromPreferences(context: Context): String? {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getString("userId", null)
    }


    // menyimpan data user ke API (remote)
    /*karena register itu methodnya post, maka di repository ini dibuat sebuah method untuk mengirim data dari lokal
    ke network (API), misalnya setelah menambahkan atau memperbarui data. di sini juag menangani response api*/
    fun registerUserProfileToRemote(userProfile: UserProfile, onResponse:(UserProfileResponse?)-> Unit) {
        // result.value = Result.Loading
        val userProfileItem = ListUserProfileItem(
            userId = userProfile.id,
            name = userProfile.name,
            email = userProfile.email,
            password = userProfile.password,
            gender = userProfile.gender,
            umur = userProfile.umur,
            berat = userProfile.berat,
            tinggi = userProfile.tinggi,
            lingkarPinggang = userProfile.lingkarPinggang,
            riwayatDiabetes = userProfile.riwayatDiabetes == 1,
            tekananDarahTinggi = userProfile.tekananDarahTinggi == 1,
            gulaDarahTinggi = userProfile.gulaDarahTinggi == 1,
            tingkatAktivitas = userProfile.tingkatAktivitas,
            konsumsiBuah = userProfile.konsumsiBuah == 1,
            gulaHarian = userProfile.gulaHarian,
            kaloriHarian = userProfile.kaloriHarian,
            karbohidratHarian = userProfile.karbohidratHarian,
            lemakHarian = userProfile.lemakHarian,
            proteinHarian = userProfile.proteinHarian
        )

        apiService.register(userProfileItem).enqueue(object:Callback<UserProfileResponse>{
            override fun onResponse(
                call: Call<UserProfileResponse>,
                response: Response<UserProfileResponse>
            ) {
                if (response.isSuccessful) {
                    onResponse(response.body())
                    // panggil id di api
                    val id: String? = response.body()?.userId
                    if (id != null) {
                        userProfile.id = id
                        saveUserIdToPreferences(context, userProfile.id)
                        val idNow = getUserIdFromPreferences(context)
                        Log.d("ID_SEKARANG", "Id User at API: $idNow")
                        GlobalScope.launch(Dispatchers.IO) {
                            insertUserProfile(userProfile)
                        }
                    }
                    Log.d("INSERT_ID_FROM_API_TO_ROOM", "Id User at Room database: ${userProfile.id}")
                } else {
                    Log.e("API_ERROR", "Server error: ${response.code()} ${response.message()}")
                    onResponse(null)
                }
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                Log.e("API_ERROR", "Failed to register user profile: ${t.message}")
                onResponse(null)
            }
        })

    }

    // cek apakah email sudah terdaftar atau belum
    suspend fun getUserProfileByEmail(email: String): UserProfile {
        return userProfileDao.getUserProfileByEmail(email)
    }

    suspend fun login(email: String, password: String): Result<UserProfile> {
        return try {
            val response = apiService.login(ListUserProfileItem(email = email, password = password))

            if (response.isSuccessful && response.body()?.status == "success") {
                val userItem = response.body()?.data
                val userProfile = UserProfile(
                    id = userItem?.userId.orEmpty(),
                )

                val userId_: String? = response.body()?.userId
                if (userId_ != null) {
                    userProfile.id = userId_
                    saveUserIdToPreferences(context, userProfile.id)
                    val idNow = getUserIdFromPreferences(context)
                    Log.d("ID_SEKARANG", "Id User at API: $idNow")
                    GlobalScope.launch(Dispatchers.IO) {
                        insertUserProfile(userProfile)
                    }
                    Log.d("id_check", "id profile inserted: ${userProfile.id}")
                }

                Result.success(userProfile)
            } else {
                Result.failure(Exception(response.body()?.status ?: "Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserProfileFromApi(userId: String?): Result<UserProfile> {
        return try {
            val response = apiService.getDataUser(userId)
            if (response.isSuccessful) {
                val userItem = response.body()?.data
                val userProfile = response.body()?.data?.let{
                    UserProfile(
                        id = userItem?.userId.toString(),
                        name = userItem?.name,
                        email = userItem?.email,
                        umur = userItem?.umur,
                        berat = userItem?.berat,
                        tinggi = userItem?.tinggi,
                        gender = userItem?.gender,
                        lingkarPinggang = userItem?.lingkarPinggang,
                        riwayatDiabetes = if (userItem?.riwayatDiabetes == true) 1 else 0,
                        gulaDarahTinggi = if (userItem?.gulaDarahTinggi == true) 1 else 0,
                        tekananDarahTinggi = if (userItem?.tekananDarahTinggi == true) 1 else 0,
                        tingkatAktivitas = userItem?.tingkatAktivitas,
                        konsumsiBuah = if (userItem?.konsumsiBuah == true) 1 else 0,
                        kaloriHarian = userItem?.kaloriHarian ?: 0,
                        karbohidratHarian = userItem?.karbohidratHarian ?: 0,
                        proteinHarian = userItem?.proteinHarian ?: 0,
                        lemakHarian = userItem?.lemakHarian ?: 0,
                        gulaHarian = userItem?.gulaHarian ?: 0,
                    )
                }
                Result.success(userProfile!!)
            } else {
                Result.failure(Exception("API ERROR: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: UserProfileRepository? = null
        fun getInstance(
            userProfileDao: UserProfileDao,
            apiService: ApiService,
            context: Context): UserProfileRepository =
            instance ?: synchronized(this) {
                instance ?: UserProfileRepository(userProfileDao, apiService, context)
            }.also { instance = it}

    }

}
package com.dicoding.sahabatgula.repository

import android.content.Context
import com.dicoding.sahabatgula.data.remote.response.ListUserProfileItem
import com.dicoding.sahabatgula.data.remote.response.UserProfileResponse
import com.dicoding.sahabatgula.data.remote.retrofit.ApiService
import com.dicoding.sahabatgula.helper.SharedPreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository(private val apiService: ApiService, private val sharedPreferencesHelper: SharedPreferencesHelper) {

    fun login(userLogin: ListUserProfileItem, context: Context) {
        apiService.login(userLogin).enqueue(object : Callback<UserProfileResponse> {
            override fun onResponse(
                call: Call<UserProfileResponse>,
                response: Response<UserProfileResponse>
            ) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    loginResponse?.let {
                        // Simpan userId ke SharedPreferences menggunakan Context yang diteruskan
                        sharedPreferencesHelper.saveUserId(context, it.userId!!)
                    }
                } else {
                    //
                }
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                //
            }
        })
    }
}
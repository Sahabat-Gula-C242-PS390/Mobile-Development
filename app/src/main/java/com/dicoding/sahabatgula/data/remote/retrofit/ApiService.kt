package com.dicoding.sahabatgula.data.remote.retrofit

import com.dicoding.sahabatgula.data.remote.response.ListUserProfileItem
import com.dicoding.sahabatgula.data.remote.response.UserProfileResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("auth/signup")
    fun register(
        @Body request: ListUserProfileItem
    ): Call<UserProfileResponse>
}
package com.dicoding.sahabatgula.data.remote.retrofit

import com.dicoding.sahabatgula.data.remote.response.ListUserProfileItem
import com.dicoding.sahabatgula.data.remote.response.UserProfileResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("auth/signup")
    fun register(
        @Body request: ListUserProfileItem
    ): Call<UserProfileResponse>

    @POST("auth/login")
    suspend fun login(
        @Body requestLogin: ListUserProfileItem
    ): Response<UserProfileResponse>

    @GET("/user/{userId}")
    suspend fun getDataUser(
        @Path("userId") userId: String? = ""
    ): Response<UserProfileResponse>
}
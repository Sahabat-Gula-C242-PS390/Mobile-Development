package com.dicoding.sahabatgula.data.remote.retrofit

import com.dicoding.sahabatgula.data.remote.response.PredictionResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ScanApiService {
        @Multipart
        @POST("predict")
        fun sendImageToApi(
            @Part image: MultipartBody.Part
        ): Call<PredictionResponse>

        companion object {
            fun create(): ScanApiService {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://sahabat-gula-388071001917.asia-southeast2.run.app/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                return retrofit.create(ScanApiService::class.java)
            }
        }
}


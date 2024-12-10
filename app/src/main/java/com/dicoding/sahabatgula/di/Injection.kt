package com.dicoding.sahabatgula.di

import android.content.Context
import com.dicoding.sahabatgula.data.local.room.UserProfileDatabase
import com.dicoding.sahabatgula.data.remote.retrofit.ApiConfig
import com.dicoding.sahabatgula.repository.UserProfileRepository

object Injection {
    fun provideRepository(context: Context): UserProfileRepository {
        val database = UserProfileDatabase.getInstance(context)
        val dao = database.userProfileDao()
        val apiService = ApiConfig.getApiService()
        return UserProfileRepository.getInstance(dao, apiService, context)

    }

}
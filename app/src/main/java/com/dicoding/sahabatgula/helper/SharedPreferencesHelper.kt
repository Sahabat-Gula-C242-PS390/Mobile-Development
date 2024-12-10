package com.dicoding.sahabatgula.helper

import android.content.Context
import android.content.SharedPreferences
import com.dicoding.sahabatgula.data.local.room.UserProfileDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object SharedPreferencesHelper {
    private const val PREFS_NAME = "sahabat_gula_prefs"
    private const val USER_ID_KEY = "user_id"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveUserId(context: Context, userId: String) {
        val editor = getPreferences(context).edit()
        editor.putString(USER_ID_KEY, userId)
        editor.apply()
    }

    fun getUserId(context: Context): String {
//        return getPreferences(context).getLong(USER_ID_KEY, )
        return getPreferences(context).getString(USER_ID_KEY, "") ?: ""
    }

    fun saveUserSession(context: Context, userId: String) {
        val sharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        sharedPreferences.edit().apply(){
            putString("userId", userId)
            putBoolean("isLoggedIn", true)
            apply()
        }
    }

    fun getCurrentUserId(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        return sharedPreferences.getString("userId", null)
    }

    suspend fun fetchUserData(userId: String, userDao: UserProfileDao) {
        return withContext(Dispatchers.IO){
            userDao.getUserProfile(userId)
        }
    }

    fun clearSession(context: Context) {
        val sharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        sharedPreferences.edit().apply{
            clear()
            apply()
        }
    }

    fun clearUserId(context: Context) {
        val editor = getPreferences(context).edit()
        editor.remove(USER_ID_KEY)
        editor.apply()
    }
}
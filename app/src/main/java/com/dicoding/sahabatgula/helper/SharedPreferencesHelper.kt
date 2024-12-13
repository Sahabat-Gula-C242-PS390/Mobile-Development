package com.dicoding.sahabatgula.helper

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SharedPreferencesHelper {
    private const val PREFS_NAME = "sahabat_gula_prefs"
    private const val USER_ID_KEY = "user_id"

    const val NAME_KEY = "name_key"
    private const val KARBO_KEY = "karbo_key"
    private const val LEMAK_KEY = "lemak_key"
    private const val GULA_KEY = "gula_key"
    private const val PROTEIN_KEY = "protein_key"
    private const val TIMESTAMP_KEY = "timestamp_key"
    private const val CATATAN_KEY = "catatan_key"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // Fungsi untuk mengecek apakah data dengan key tertentu sudah ada
    fun isDataExist(context: Context, key: String): Boolean {
        val sharedPreferences = getPreferences(context)
        return sharedPreferences.contains(key)  // Mengecek apakah key sudah ada
    }

    fun saveScanData( context: Context, name: String?, karbo: Int, lemak: Double, gula: Int, protein: Int) {
        val sharedPreferences = getPreferences(context)
        val editor = sharedPreferences.edit()

        // Membuat objek untuk menyimpan data
        val scanData = ScanData(name, karbo, lemak, gula, protein)

        // Ambil data yang sudah ada di SharedPreferences (jika ada)
        val currentListJson = sharedPreferences.getString(CATATAN_KEY, "[]")
        val currentList: MutableList<ScanData> = Gson().fromJson(currentListJson, object : TypeToken<MutableList<ScanData>>() {}.type)

        // Menambahkan data baru ke list
        currentList.add(scanData)

        // Simpan kembali ke SharedPreferences setelah ditambahkan
        val newListJson = Gson().toJson(currentList)
        Log.d("SharedPrefs", "Serialized Data: $newListJson")
        editor.putString(CATATAN_KEY, newListJson)
        editor.apply()

        Log.d("SharedPrefs", "Saving ScanData: $scanData")
    }


    fun getScanData(context: Context): List<ScanData> {
        val sharedPreferences = getPreferences(context)
        val jsonString = sharedPreferences.getString(CATATAN_KEY, "[]")

        // Pastikan deserialisasi menggunakan TypeToken yang sesuai
        val type = object : TypeToken<List<ScanData>>() {}.type
        return Gson().fromJson(jsonString, type) ?: emptyList() // Jika null, kembalikan list kosong
    }

    fun isDataExpired(context: Context): Boolean {
        val timestamp = getPreferences(context).getLong(TIMESTAMP_KEY, 0)
        return System.currentTimeMillis() - timestamp > 24 * 60 * 60 * 1000  // 24 jam dalam millisecond
    }

    fun saveUserId(context: Context, userId: String) {
        val editor = getPreferences(context).edit()
        editor.putString(USER_ID_KEY, userId)
        editor.apply()
    }

    fun getUserId(context: Context): String {
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

data class ScanData(
    val name: String?,
    val karbo: Int,
    val lemak: Double,
    val gula: Int,
    val protein: Int
)
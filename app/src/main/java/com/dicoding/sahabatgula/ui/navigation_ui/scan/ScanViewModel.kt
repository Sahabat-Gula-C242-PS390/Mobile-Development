package com.dicoding.sahabatgula.ui.navigation_ui.scan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.sahabatgula.data.remote.response.PredictionItem
import com.dicoding.sahabatgula.data.remote.retrofit.ScanApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class ScanViewModel : ViewModel() {

    private val scanApiService = ScanApiConfig.getApiService()

    // LiveData untuk menyimpan hasil scan
    private val _data = MutableLiveData<List<PredictionItem>>()
    val data: LiveData<List<PredictionItem>> get() = _data

    // Fungsi mengirim gambar ke server
    fun sendImageToServer(imageBytes: ByteArray) {
        val requestBody = imageBytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image", "image.jpg", requestBody)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = scanApiService.sendImageToApi(body).execute()
                if (response.isSuccessful) {
                    // Perbarui LiveData dengan hasil dari API
                    val result = response.body()?.data ?: emptyList()
                    Log.d("ScanViewModel", "Updating LiveData with: $result")
                    _data.postValue(result)
                    Log.d("ScanViewModel", "Image uploaded successfully: $result")
                } else {
                    Log.e("ScanViewModel", "Failed to upload image: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("ScanViewModel", "Error uploading image", e)
            }
        }
    }
}
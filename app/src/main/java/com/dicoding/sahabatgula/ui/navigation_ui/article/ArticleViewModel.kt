package com.dicoding.sahabatgula.ui.navigation_ui.article

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.sahabatgula.data.remote.response.ArticleResponse
import com.dicoding.sahabatgula.data.remote.response.DataItem
import com.dicoding.sahabatgula.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleViewModel : ViewModel() {

    private val _listArticle = MutableLiveData<List<DataItem>>()
    val listArticle: MutableLiveData<List<DataItem>> = _listArticle

    fun setListArticle() {
        val client = ApiConfig.getApiService().getArticles()
        client.enqueue(object : Callback<ArticleResponse> {
            override fun onResponse(call: Call<ArticleResponse>, response: Response<ArticleResponse>) {
                if(response.isSuccessful) {
                    _listArticle.value = response.body()?.data as List<DataItem>
                }
            }

            override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

}
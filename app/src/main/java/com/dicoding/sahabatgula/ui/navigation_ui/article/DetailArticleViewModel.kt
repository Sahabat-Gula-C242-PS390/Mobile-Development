package com.dicoding.sahabatgula.ui.navigation_ui.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.sahabatgula.data.remote.response.Article
import com.dicoding.sahabatgula.data.remote.response.DetailArticleResponse
import com.dicoding.sahabatgula.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailArticleViewModel: ViewModel() {

    private val _article = MutableLiveData<Article>()
    val article: LiveData<Article> = _article

    fun setArticleData(id: String?) {
        val client =ApiConfig.getApiService().getArticleById(id!!)
        client.enqueue(object: Callback<DetailArticleResponse>{
            override fun onResponse(
                call: Call<DetailArticleResponse>,
                response: Response<DetailArticleResponse>
            ) {
                if (response.isSuccessful){
                    _article.value = response.body()?.article!!
                }
            }

            override fun onFailure(p0: Call<DetailArticleResponse>, p1: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}
package com.dicoding.sahabatgula.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetailArticleResponse(

	@field:SerializedName("data")
	val article: Article? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("error")
	val message: String? = null
)

data class Article(

	@field:SerializedName("originalLink")
	val originalLink: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("subtitle")
	val subtitle: String? = null,

	@field:SerializedName("articleId")
	val articleId: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("content")
	val content: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)

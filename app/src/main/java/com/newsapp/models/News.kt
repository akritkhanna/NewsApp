package com.newsapp.models

import com.google.gson.annotations.SerializedName

data class News(

	@field:SerializedName("totalResults")
	val totalResults: Int? = null,

	@field:SerializedName("articles")
	val articles: List<ArticlesItem> = emptyList(),

	@field:SerializedName("status")
	val status: String? = null
)
package com.newsapp.repositories

import com.newsapp.api.ApiInterface
import javax.inject.Inject


class NewsRepository @Inject constructor(private val apiInterface: ApiInterface) {


    suspend fun getBreakingNews(countryCode: String, pageSize: Int) =

        apiInterface.getNews(countryCode = countryCode, pageSize = pageSize)


}
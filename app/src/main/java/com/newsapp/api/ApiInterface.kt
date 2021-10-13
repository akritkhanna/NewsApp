package com.newsapp.api

import com.newsapp.util.Constants.Companion.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("v2/top-headlines/")
    suspend fun getNews(
        @Query("apiKey") apiKey : String = API_KEY,
        @Query("country") countryCode : String,
        @Query("page") pageNumber : Int
    )


}
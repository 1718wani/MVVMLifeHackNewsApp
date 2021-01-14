package com.androiddevs.mvvmnewsapp.api

import com.androiddevs.mvvmnewsapp.NewsResponse
import com.androiddevs.mvvmnewsapp.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {
    //工夫を凝らしているポイント,
    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        //このやりかたでカテゴリーを絞れているのかわからないので要確認
        @Query("category")
        category: String = "career",
        @Query("country")
        countryCode:String = "jp",
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>

    @GET("v2/top-headlines")
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>
}
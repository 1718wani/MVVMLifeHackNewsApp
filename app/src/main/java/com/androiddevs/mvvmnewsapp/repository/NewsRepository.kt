package com.androiddevs.mvvmnewsapp.repository

import com.androiddevs.mvvmnewsapp.api.RetrofitInstance
import com.androiddevs.mvvmnewsapp.db.ArticleDatabase

class NewsRepository(
    val db: ArticleDatabase
) {
    suspend fun getBreakingNews( categoryName:String,  countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(categoryName, countryCode,pageNumber)

    suspend fun searchNews( searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery,pageNumber)

//    suspend fun searchNews(countryCode: String,  searchQuery: String, pageNumber: Int) =
//        RetrofitInstance.api.searchForNews(countryCode,searchQuery,pageNumber)


}
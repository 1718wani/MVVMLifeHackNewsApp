package com.androiddevs.mvvmnewsapp.repository

import com.androiddevs.mvvmnewsapp.api.RetrofitInstance
import com.androiddevs.mvvmnewsapp.db.ArticleDatabase

class NewsRepository(
    val db: ArticleDatabase
) {
    suspend fun getBreakingNews( countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)
//本当はカテゴリーをしぼった表示をしたいけど、一旦引数を順番にいれる必要あるので保留
//    suspend fun getBreakingNews(categoryName: String, countryCode: String, pageNumber: Int) =
//        RetrofitInstance.api.getBreakingNews(categoryName,countryCode,pageNumber)
}
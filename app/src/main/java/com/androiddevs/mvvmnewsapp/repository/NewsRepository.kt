package com.androiddevs.mvvmnewsapp.repository

import com.androiddevs.mvvmnewsapp.api.RetrofitInstance
import com.androiddevs.mvvmnewsapp.db.ArticleDatabase
import com.androiddevs.mvvmnewsapp.models.Article


//設計： Retrofitから引っ張ってくるファンクションも、データベースを操作するファンクションもこちらに記載。
//Daoにある文言とかを結局データベースを経由して作っている？（自信ない）

class NewsRepository(
    val db: ArticleDatabase
) {
    suspend fun getBreakingNews( categoryName:String,  countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(categoryName, countryCode,pageNumber)

    suspend fun searchNews( searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery,pageNumber)

//    suspend fun searchNews(countryCode: String,  searchQuery: String, pageNumber: Int) =
//        RetrofitInstance.api.searchForNews(countryCode,searchQuery,pageNumber)

    //一回Databaseを経由してgetArticleDaoを使ってDAOにアクセス。
    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)

}
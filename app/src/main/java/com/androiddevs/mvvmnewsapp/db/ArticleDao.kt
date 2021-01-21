package com.androiddevs.mvvmnewsapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.androiddevs.mvvmnewsapp.models.Article
import java.lang.StringBuilder

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE )
    suspend fun upsert(article: Article): Long

    @Query("SELECT * FROM articles")
    fun getAllArticles():LiveData<List<Article>>

    @Query("SELECT * FROM articles WHERE url == :specifiedUrl ")
    fun getAllArticlesBasedOnUrl(specifiedUrl: String):LiveData<List<Article>>


    //全選択ではなく任意のデータを消している
    @Delete
    suspend fun deleteArticle(article: Article)
}
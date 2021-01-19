package com.androiddevs.mvvmnewsapp.repository

import android.content.Context
import android.util.Log
import androidx.datastore.DataStore
import androidx.datastore.createDataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import com.androiddevs.mvvmnewsapp.api.RetrofitInstance
import com.androiddevs.mvvmnewsapp.db.ArticleDatabase
import com.androiddevs.mvvmnewsapp.models.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.lang.StringBuilder
import androidx.datastore.preferences.preferencesKey


//設計： Retrofitから引っ張ってくるファンクションも、データベースを操作するファンクションもこちらに記載。
//Daoにある文言とかを結局データベースを経由して作っている？（自信ない）

const val PREFERENCE_NAME = "my_preference"

class NewsRepository(
    val db: ArticleDatabase,
    context: Context
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

    //特定のUrlが存在しているか確認する
    fun getSpecifiedUrlNews(specifiedUrl: String) = db.getArticleDao().getAllArticlesUrl(specifiedUrl)


    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)

    private object PreferenceKeys{
        val name = preferencesKey<String>("my_name")
    }

    private val dataStore: DataStore<Preferences> = context.createDataStore(
        name = PREFERENCE_NAME
    )

    suspend fun saveToDataStore(name: String){
        dataStore.edit { preference ->
            preference[PreferenceKeys.name] = name
        }
    }

    val readFromDataStore: Flow<String> = dataStore.data
        .catch { exception ->
            if(exception is IOException){
                Log.d("DataStore", exception.message.toString())
                emit(emptyPreferences())
            }else {
                throw exception
            }
        }
        .map { preference ->
            val myName = preference[PreferenceKeys.name] ?: "none"
            myName
        }

}
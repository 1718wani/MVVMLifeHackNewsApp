package com.androiddevs.mvvmnewsapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.*
import com.androiddevs.mvvmnewsapp.NewsApplication
import com.androiddevs.mvvmnewsapp.models.Article
import com.androiddevs.mvvmnewsapp.models.NewsResponse
import com.androiddevs.mvvmnewsapp.repository.NewsRepository
import com.androiddevs.mvvmnewsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException


class NewsViewModel(
    app: Application,
    //データベース系いじるときは必ずこのnewsRepositoryの変数を使うようにしている
    //#13でインターネット接続について確認したが、それによってRoomに保存したやつだけは見れるようになった。
    val newsRepository: NewsRepository
    //AndroidViewModelにした理由を知りたい。
) : AndroidViewModel(app) {
    val breakingNews : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null

    val searchNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null




    init {
        // CategoryをBusinessに事前に指定
        getBreakingNews("general","jp")
//        searchNews("jp","")
    }




    fun getBreakingNews(category:String, countryCode:String) = viewModelScope.launch {
        safeBreakingNewsCall("general","jp")
        //下のやつはインターネットコネクションなしのキャッシュ作らなかった場合に使うやつ
//        breakingNews.postValue(Resource.Loading())
//        val response = newsRepository.getBreakingNews(category,countryCode,breakingNewsPage)
//        breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    fun searchNews(searchQuery : String) = viewModelScope.launch {
        safeSearchNewsCall(searchQuery)
//        searchNews.postValue(Resource.Loading())
//        val response = newsRepository.searchNews(searchQuery,searchNewsPage)
//        searchNews.postValue(handleSearchNewsResponse(response))
    }

    //これはCountryCode入れて限定しようとしたときのコーディング
//    fun searchNews(countryCode:String, searchQuery : String) = viewModelScope.launch {
//        searchNews.postValue(Resource.Loading())
//        val response = newsRepository.searchNews(countryCode,searchQuery,searchNewsPage)
//        searchNews.postValue(handleSearchNewsResponse(response))
//    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful){
            //疑問点：このresultResponseというのがなにかわからない
            response.body()?.let { resultResponse->
                breakingNewsPage++
                if (breakingNewsResponse == null){
                    breakingNewsResponse = resultResponse
                }else{
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                //NullじゃなかったらbreakingNewsResponseを返して、Nullだった場合resultResponseを返す
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse->
                searchNewsPage++
                if (searchNewsResponse == null){
                    searchNewsResponse  = resultResponse
                }else{
                    val oldArticles =searchNewsResponse ?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                //NullじゃなかったらbreakingNewsResponseを返して、Nullだった場合resultResponseを返す
                return Resource.Success(searchNewsResponse  ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun notYetArticle(specifiedUrl: String?):Boolean  {
        val news  = newsRepository.getSpecifiedUrlNews(specifiedUrl)
        val list = listOf(news)
        var size = list.size
        return size <= 1
    }



    //getSavednews()によって登録されているデータベースすべて引っ張ってきている。
    // データベースを選択しているだけでデータベースをいじっていないからCoroutineに入っていない？。
    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }



    private suspend fun safeSearchNewsCall(searchQuery: String){
        breakingNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepository.searchNews(searchQuery, searchNewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))
            }else{
                searchNews.postValue(Resource.Error("インターネット接続がありません"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> searchNews.postValue(Resource.Error("ネットワーク接続に失敗しました"))
                else -> searchNews.postValue(Resource.Error("接続に失敗しました"))
            }

        }
    }

    //インターネットつながってない系の処理
    private suspend fun safeBreakingNewsCall(category: String, countryCode: String){
        breakingNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepository.getBreakingNews(category, countryCode, breakingNewsPage)
                breakingNews.postValue(handleBreakingNewsResponse(response))
            }else{
                breakingNews.postValue(Resource.Error("インターネット接続がありません"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> breakingNews.postValue(Resource.Error("ネットワーク接続に失敗しました"))
                else -> breakingNews.postValue(Resource.Error("接続に失敗しました"))
            }

        }
    }

    private fun hasInternetConnection(): Boolean{
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }else{
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }


}
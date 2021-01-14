package com.androiddevs.mvvmnewsapp


data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)
package com.androiddevs.mvvmnewsapp.ui


import com.google.gson.annotations.SerializedName

data class Article(
    val source: Source,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    val content: String
)
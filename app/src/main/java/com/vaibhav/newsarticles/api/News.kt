package com.vaibhav.newsarticles.api

import java.io.Serializable

// model class of News
data class News(
    val source: Source,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    val content: String,
):Serializable

// model class of Source
data class Source(
    val id: String,
    val name: String,
):Serializable
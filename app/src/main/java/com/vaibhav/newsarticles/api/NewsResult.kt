package com.vaibhav.newsarticles.api

// A basic News Response class
sealed class NewsResult<T>(
    val data: Any? = null,
    val message: String? = null
) {
    // Success Result
    class Success<T>(data: Any) : NewsResult<T>(data)

    // Error Result
    class Error<T>(message: String?, data: T? = null) : NewsResult<T>(data, message)

    // Loading Result
    class Loading<T>():NewsResult<T>()
}
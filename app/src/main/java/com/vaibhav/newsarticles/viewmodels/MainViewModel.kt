package com.vaibhav.newsarticles.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.newsarticles.api.News
import com.vaibhav.newsarticles.api.NewsRepository
import com.vaibhav.newsarticles.api.NewsResult
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    // Created a new MutableLiveData with the reference of NewsResult
    private val _data = MutableLiveData<NewsResult<Any>>()

    // Read only LiveData
    val data : LiveData<NewsResult<Any>> = _data

    fun loadNews(){
        // Called a suspended function from viewModelScope for asynchronous operation.
        viewModelScope.launch {

            // fetch result from NewsRepository
             val result = NewsRepository().fetch<Any>()
            _data.value= result
        }
    }


}
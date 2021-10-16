package com.newsapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newsapp.models.News
import com.newsapp.repositories.NewsRepository
import com.newsapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val repository: NewsRepository) : ViewModel() {


    val topHeadlines: MutableLiveData<Resource<News>> = MutableLiveData();


    init {
        getTopHeadlines("in", 50)
    }


    private fun getTopHeadlines(countryCode: String, pageSize: Int) = viewModelScope.launch {

        topHeadlines.postValue(Resource.Loading())

        val response = repository.getBreakingNews(countryCode, pageSize)

        if (response.isSuccessful) {
            response.body()?.let {
                topHeadlines.postValue(Resource.Success(it))
            }
        } else {
            topHeadlines.postValue(Resource.Error(response.message()))
        }

    }


}
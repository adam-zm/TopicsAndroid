package com.example.topics.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.topics.database.DatabaseConnector
import com.example.topics.Topic
import kotlinx.coroutines.launch
import retrofit2.HttpException


class HomeScreenViewModel(
    val databaseConnector: DatabaseConnector
): ViewModel() {
    var topics = mutableStateOf<List<Topic>>(listOf())
    val isRefreshing = mutableStateOf(false)
    var newTopicTitle = mutableStateOf("")

    fun fetchTopics(){
        viewModelScope.launch{
            isRefreshing.value = true
            topics.value = databaseConnector.fetchTopics()
            isRefreshing.value = false
        }
    }

    fun addNewTopic(title: String){
        viewModelScope.launch {
            try {
                databaseConnector.addNewTopic(title)
            }catch (e: HttpException){
                Log.e("HomeViewModel new topic", "${e.message()}")
            }
        }
    }

    init {
        fetchTopics()
    }
}
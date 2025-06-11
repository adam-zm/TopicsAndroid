package com.example.topics.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.topics.SharedStateHandler
import com.example.topics.database.DatabaseConnector
import com.example.topics.Topic
import com.example.topics.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException


class HomeScreenViewModel(
    private val databaseConnector: DatabaseConnector,
    private val sharedStateHandler: SharedStateHandler
): ViewModel() {
    var newTopicTitle = mutableStateOf("")

    fun fetchTopics(){
        viewModelScope.launch{
            sharedStateHandler.setIsRefreshing(true)
            delay(1000)
            try{
                sharedStateHandler.updateTopics(databaseConnector.fetchTopics())
            }catch (e: HttpException){
                Log.e("HomeViewModel", "Fetch topics ${e.message()}")
            }
            sharedStateHandler.setIsRefreshing(false)
        }
    }

    fun addNewTopic(title: String){
        viewModelScope.launch {
            try {
                databaseConnector.addNewTopic(title)
            }catch (e: HttpException){
                Log.e("HomeViewModel new topic", e.message())
            }
        }
    }

    fun toggleUseHapticFeedback(useHaptic: Boolean){
        viewModelScope.launch {
            sharedStateHandler.toggleHapticFeedbackBool(useHaptic)
        }
    }

    init {
        fetchTopics()
        viewModelScope.launch {
            sharedStateHandler.onStartGetHapticBool()
        }
    }
}
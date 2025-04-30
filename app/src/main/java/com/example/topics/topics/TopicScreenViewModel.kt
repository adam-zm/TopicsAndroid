package com.example.topics.topics

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.topics.Comment
import com.example.topics.database.DatabaseConnector
import com.example.topics.Topic
import kotlinx.coroutines.launch
import retrofit2.HttpException

class TopicScreenViewModel(
    val databaseConnector: DatabaseConnector
) : ViewModel() {
    val isLoading = mutableStateOf(false)
    var isRefreshing = mutableStateOf(false)
    var topic: MutableState<Topic> = mutableStateOf(Topic(id = -1, title = ""))
    var newCommentText = mutableStateOf("")
    var comments = mutableStateOf<List<Comment>>(listOf<Comment>())

    fun fetchTopic(id: Int?) {
        if (id != null) {
            viewModelScope.launch {
                isLoading.value = true
                topic.value = databaseConnector.fetchTopicById(id)

                if (topic.value.title.length > 25) {
                    topic.value.title = topic.value.title.dropLast(topic.value.title.length - 23)
                    topic.value.title += "..."
                }
                isLoading.value = false
            }
        }else{
            Log.e("TopicScreenViewModel", "Got null id")
        }
    }

    fun deleteTopicById(){
        viewModelScope.launch {
            if(topic.value.id != -1){
                try {
                    databaseConnector.deleteTopicById(topic.value.id)
                }catch (e: HttpException){
                    Log.e("HomeViewModel Delete", e.message())
                }
            }else{
                Log.e("TopicScreenViewModel", "No topic id to delete")
            }
        }
    }

//    fun fetchComments(topicId: Int?){
//        if(topicId != null){
//            viewModelScope.launch{
//                isLoading.value = true
//                comments.value = databaseConnector.fetchComments(topicId)
//                isLoading.value = false
//            }
//        }else{
//            Log.e("Topics View Model", "Null id specified")
//        }
//    }

//    fun addNewComment(text: String){
//        viewModelScope.launch {
//            topic.value?.id?.let {
//                databaseConnector.addNewComment(topicId = it, text = text)
//            } ?: Log.e("Topics View Model", "Id is null")
//        }
//    }
}
package com.example.topics.database

import com.example.topics.Comment
import com.example.topics.Topic
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from

class DatabaseConnector(
    val apiClient: ApiClient
){
    suspend fun fetchTopics(): List<Topic> {
//        val response = apiClient.getTopics()
//
//        return if(response.isSuccessful && !response.body().isNullOrEmpty()){
//            response.body()!!
//        }else{
//            emptyList()
//        }
        val topics = mutableListOf<Topic>()

        for(i in 0..15){
            topics.add(Topic(id = i, title = "This is a topic $i", text = "This is an example topic text to see how " +
                    "the text cutting functionality works"))
        }

        return topics
    }

    suspend fun addNewTopic(title: String) {
        apiClient.postNewTopic(
            topic = Topic(
                title = title,
                id = 1,
                text = ""
            )
        )
    }

    suspend fun fetchTopicById(id: Int): Topic{
//        val response = apiClient.getTopicById(id = id)
//
//        return if(response.isSuccessful){
//            response.body()!!
//        }else{
//            Topic(id = -1, title = "", text = "")
//        }

        val topic = Topic(id = id, title = "This is a topic $id", text = "This is an example topic text to see how " +
                "the text cutting functionality works")

        if (topic.title.length > 25) {
            topic.title = topic.title.dropLast(topic.title.length - 23)
            topic.title += "..."
        }

        return topic
    }

    suspend fun deleteTopicById(id: Int){
        //TODO: implement did succeed checking

        val response = apiClient.deleteTopicById(id)
    }
//
//    suspend fun addNewComment(topicId: Int, text: String) {
//        supabase.from("Comments").upsert(
//            Comment(topic_id = topicId, content = text)
//        )
//    }
//
//    suspend fun fetchComments(topicId: Int): List<Comment> {
//        val comments = supabase.from("Comments").select {
//            filter {
//                Comment::topic_id eq topicId
//            }
//        }.decodeList<Comment>()
//
//        return comments
//    }
}
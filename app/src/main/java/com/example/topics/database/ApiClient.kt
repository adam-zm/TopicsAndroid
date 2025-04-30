package com.example.topics.database

import com.example.topics.Topic
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiClient {

    @GET("topics")
    suspend fun getTopics(): Response<List<Topic>>

    @POST("newTopic")
    suspend fun postNewTopic(@Body topic: Topic)

    @GET("topic/{id}")
    suspend fun getTopicById(@Path("id") id: Int): Response<Topic>

    @POST("deleteTopic/{id}")
    suspend fun deleteTopicById(@Path("id") id: Int)
}
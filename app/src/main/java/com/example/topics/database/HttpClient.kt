package com.example.topics.database

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HttpClient {
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.0.189:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: ApiClient = retrofit.create(ApiClient::class.java)
}
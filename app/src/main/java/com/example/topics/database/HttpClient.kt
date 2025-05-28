package com.example.topics.database

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HttpClient {
    fun getService(): ApiClient? {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.193:6000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ApiClient::class.java)
    }
}
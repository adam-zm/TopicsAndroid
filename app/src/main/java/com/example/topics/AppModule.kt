package com.example.topics

import com.example.topics.database.DatabaseConnector
import com.example.topics.database.HttpClient
import com.example.topics.home.HomeScreenViewModel
import com.example.topics.topics.TopicScreenViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::DatabaseConnector)
    single{
        HttpClient().getService()
    }
    viewModelOf(::HomeScreenViewModel)
    viewModelOf(::TopicScreenViewModel)
}
package com.example.topics

import androidx.compose.ui.platform.LocalContext
import com.example.topics.database.DatabaseConnector
import com.example.topics.database.HttpClient
import com.example.topics.home.HomeScreenViewModel
import com.example.topics.settings.SettingsScreen
import com.example.topics.settings.SettingsScreenViewModel
import com.example.topics.topics.TopicScreenViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::DatabaseConnector)
    single{
        HttpClient().getService()
    }
    single{
        SharedStateHandler(androidApplication())
    }
    viewModelOf(::HomeScreenViewModel)
    viewModelOf(::TopicScreenViewModel)
    viewModelOf(::SettingsScreenViewModel)
}
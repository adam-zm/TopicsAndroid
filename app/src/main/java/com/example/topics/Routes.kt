package com.example.topics

import kotlinx.serialization.Serializable

sealed interface Routes {
    @Serializable
    object HomeScreen

    @Serializable
    object SettingsScreen

    @Serializable
    data class TopicScreen(
        val topicId: Int?
    )
}

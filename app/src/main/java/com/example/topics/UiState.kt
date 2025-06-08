package com.example.topics

data class UiState(
    var topics: List<Topic> = emptyList(),
    var isRefreshing: Boolean = false,
    var useHapticFeedback: Boolean = true,
    var currentScreen: Int = 0
)

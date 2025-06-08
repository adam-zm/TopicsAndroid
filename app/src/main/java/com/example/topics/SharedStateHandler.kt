package com.example.topics

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SharedStateHandler() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun updateTopics(topics: List<Topic>){
        _uiState.update {
            it.copy(
                topics = topics
            )
        }
    }

    fun toggleUseHaptics(boolean: Boolean){
        _uiState.update {
            it.copy(
                useHapticFeedback = boolean
            )
        }
    }

    fun setIsRefreshing(boolean: Boolean){
        _uiState.update {
            it.copy(
                isRefreshing = boolean
            )
        }
    }
    fun updateCurrentScreen(screenId: Int){
        _uiState.update {
            it.copy(
                currentScreen = screenId
            )
        }
    }
}
package com.example.topics

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class SharedStateHandler(private val context: Context) {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun updateTopics(topics: List<Topic>) {
        _uiState.update {
            it.copy(
                topics = topics
            )
        }
    }

    fun setIsRefreshing(boolean: Boolean) {
        _uiState.update {
            it.copy(
                isRefreshing = boolean
            )
        }
    }

    fun updateCurrentScreen(screenId: Int) {
        _uiState.update {
            it.copy(
                currentScreen = screenId
            )
        }
    }

    suspend fun onStartGetHapticBool() {
        val USE_HAPTIC_FEEDBACK = booleanPreferencesKey("use_haptic_feedback")
        val useHapticFlow = context.dataStore.data.map { prefs ->
            prefs[USE_HAPTIC_FEEDBACK] == true
        }
        coroutineScope {
            useHapticFlow.collect { bool ->
                _uiState.update {
                    it.copy(
                        useHapticFeedback = bool
                    )
                }
            }
        }
    }

    suspend fun toggleHapticFeedbackBool(boolean: Boolean) {
        val USE_HAPTIC_FEEDBACK = booleanPreferencesKey("use_haptic_feedback")
        context.dataStore.edit {
            it[USE_HAPTIC_FEEDBACK] = boolean
        }

        _uiState.update {
            it.copy(
                useHapticFeedback = boolean
            )
        }
    }
}
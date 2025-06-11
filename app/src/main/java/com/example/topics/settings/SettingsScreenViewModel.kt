package com.example.topics.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.topics.SharedStateHandler
import kotlinx.coroutines.launch

class SettingsScreenViewModel(
    private val sharedStateHandler: SharedStateHandler
): ViewModel() {
    fun toggleUseHapticFeedback(useHaptic: Boolean){
        viewModelScope.launch {
            sharedStateHandler.toggleHapticFeedbackBool(useHaptic)
        }
    }
}
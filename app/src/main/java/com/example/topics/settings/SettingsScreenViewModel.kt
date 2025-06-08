package com.example.topics.settings

import androidx.lifecycle.ViewModel
import com.example.topics.SharedStateHandler

class SettingsScreenViewModel(
    private val sharedStateHandler: SharedStateHandler
): ViewModel() {
    fun toggleUseHapticFeedback(useHaptic: Boolean){
        sharedStateHandler.toggleUseHaptics(useHaptic)
    }
}
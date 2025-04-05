package com.azeemi.silentsignal.presentation

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

class MainViewModel : ViewModel() {
    private val _message = mutableStateOf("")
    val message: State<String> = _message

    private val _expireAt = mutableStateOf("")
    val expireAt: State<String> = _expireAt

    // This method is used to update message and timestamp when a new announcement is received.
    fun updateMessage(newMessage: String, newTimestamp: String) {
        _message.value = newMessage
        _expireAt.value = newTimestamp
    }
}

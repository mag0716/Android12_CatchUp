package com.github.mag0716.splashscreensample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _isReady = MutableLiveData<Boolean>()
    val isReady: LiveData<Boolean> = _isReady

    private val _hasError = MutableLiveData<Boolean>()
    val hasError: LiveData<Boolean> = _hasError

    fun init() {
        viewModelScope.launch {
            delay(5000)
            _hasError.value = true
        }
    }

    fun retry() {
        _hasError.value = false
        viewModelScope.launch {
            delay(5000)
            _isReady.value = true
        }
    }
}
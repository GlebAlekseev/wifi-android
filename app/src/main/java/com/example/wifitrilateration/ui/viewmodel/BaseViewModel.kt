package com.example.wifitrilateration.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel : ViewModel() {
    protected val _errorHandler = MutableStateFlow("")
    val errorHandler: StateFlow<String>
        get() = _errorHandler
    abstract val coroutineExceptionHandler: CoroutineExceptionHandler
    protected fun CoroutineScope.launchWithExceptionHandler(block: suspend CoroutineScope.() -> Unit): Job {
        return this.launch(coroutineExceptionHandler) {
            block.invoke(this)
        }
    }

    protected fun <T> runBlockingWithExceptionHandler(block: suspend CoroutineScope.() -> T): T {
        return runBlocking(coroutineExceptionHandler) {
            block.invoke(this)
        }
    }
}
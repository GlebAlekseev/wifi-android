package com.example.wifitrilateration.ui.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import javax.inject.Inject
import com.example.wifitrilateration.domain.entity.Result
import com.example.wifitrilateration.domain.interactor.AuthUseCase
import com.example.wifitrilateration.domain.interactor.UserUseCase


class MainViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val userUseCase: UserUseCase
) : BaseViewModel() {
    override val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, exception ->
            val message = exception.message ?: "Неизвестная ошибка"
            _errorHandler.value = message
        }

    val isAuth: Boolean
        get() {
            val tokenPair = userUseCase.getTokenPair()
            return if (tokenPair == null) {
                // Удалить кеш записи в браузере
                false
            } else true
        }

    fun updateTokenPair(code: String, block: (Result<Unit>) -> Unit) {
        viewModelScope.launchWithExceptionHandler {
            authUseCase.getTokenPair(code).collect {
                block.invoke(it)
            }
        }
    }

    fun logout(block: (Result<Unit>) -> Unit) {
        viewModelScope.launchWithExceptionHandler {
            authUseCase.logout().collect {
                block.invoke(it)
            }
        }
    }
}
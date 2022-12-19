package com.example.wifitrilateration.domain.interactor

import com.example.wifitrilateration.domain.repository.AuthRepository
import com.example.wifitrilateration.domain.entity.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    fun logout():  Flow<Result<Unit>> =
        authRepository.logout()

    fun getTokenPair(code: String): Flow<Result<Unit>> =
        authRepository.getTokenPair(code)
}
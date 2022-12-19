package com.example.wifitrilateration.domain.interactor

import javax.inject.Inject
import com.example.wifitrilateration.domain.entity.TokenPair
import com.example.wifitrilateration.domain.repository.UserRepository

class UserUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    fun getUserId(): Long = userRepository.getUserId()
    fun getExpiresAt(): Long? = userRepository.getExpiresAt()
    fun clear(): Unit = userRepository.clear()
    fun getDisplayName(): String? = userRepository.getDisplayName()
    fun getTokenPair(): TokenPair? = userRepository.getTokenPair()
    fun getAccessToken(): String? = userRepository.getAccessToken()
    fun getGroup(): String? = userRepository.getGroup()
    fun getRefreshToken(): String? = userRepository.getRefreshToken()
    fun getRole(): String? = userRepository.getRole()
    fun setTokenPair(tokenPair: TokenPair): Unit = userRepository.setTokenPair(tokenPair)
}
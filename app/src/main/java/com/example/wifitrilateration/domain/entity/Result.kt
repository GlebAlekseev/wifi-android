package com.example.wifitrilateration.domain.entity

enum class ResultStatus {
    SUCCESS,        // Успех
    LOADING,        // Загрузка
    FAILURE,        // Ошибка
    UNAUTHORIZED,    // Выбросить в окно авторизации
    DISABLED_LOCATION, // Запросить включение геолокации
    DISABLED_WIFI, // Запросить включение WiFi
}

data class Result<T>(
    val status: ResultStatus,
    val data: T,
    val message: String = "",
)
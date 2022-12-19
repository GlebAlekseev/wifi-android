package com.example.wifitrilateration.domain.entity

data class RouterConfigurationtViewState(
    val result: Result<List<Router>>,
    val errorMessage: String
) {
    companion object {
        const val OK = ""
        val PLUG = RouterConfigurationtViewState(
            Result(ResultStatus.LOADING, mutableListOf()),
            OK
        )
    }
}

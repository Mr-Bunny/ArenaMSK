package com.example.arenamsk.network.models

data class ApiError(val statusCode: Int = DEFAULT_CODE,
                    val message: String = DEFAULT_ERROR_MESSAGE,
                    val exception: Exception? = null) {

    companion object {
        const val DEFAULT_CODE = 0
        const val DEFAULT_ERROR_MESSAGE = "Error"
    }
}
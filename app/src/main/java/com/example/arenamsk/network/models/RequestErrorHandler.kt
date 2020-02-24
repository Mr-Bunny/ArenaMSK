package com.example.arenamsk.network.models

interface RequestErrorHandler {
    /** If network is unavailable. */
    suspend fun networkUnavailableError()

    /**
     * Request failed.
     *
     * @param error Instance of ApiError class if exists.
     */
    suspend fun requestFailedError(error: ApiError? = null)

    /** Request timeout exception. */
    suspend fun timeoutException()

    /** Request was success but response was null. */
    suspend fun requestSuccessButResponseIsNull()
}
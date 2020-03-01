package com.example.arenamsk.network.models

interface RequestErrorHandler {
    /** Если нет подключения к интернету */
    suspend fun networkUnavailableError()

    /**
     * Запрос не выполнился
     *
     * @param error Экземпляр ошибки с подробными сведениями
     */
    suspend fun requestFailedError(error: ApiError? = null)

    /** Таймаут */
    suspend fun timeoutException()

    /** Запрос выполнился, но вернул null */
    suspend fun requestSuccessButResponseIsNull()
}
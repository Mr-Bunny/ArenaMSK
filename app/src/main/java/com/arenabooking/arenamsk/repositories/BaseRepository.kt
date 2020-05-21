package com.arenabooking.arenamsk.repositories

import com.arenabooking.arenamsk.App
import com.arenabooking.arenamsk.datasources.RemoteDataSource
import com.arenabooking.arenamsk.network.models.ApiError
import com.arenabooking.arenamsk.network.models.RequestErrorHandler
import com.arenabooking.arenamsk.network.models.auth.UpdatedTokensModel
import com.arenabooking.arenamsk.network.utils.AuthUtils
import com.arenabooking.arenamsk.network.utils.NetworkUtils
import com.arenabooking.arenamsk.network.utils.NetworkUtils.getErrorMessageFromErrorBody
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import com.arenabooking.arenamsk.network.utils.NetworkUtils.Result
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/** Базовый класс репозитория */
open class BaseRepository {

    private val job by lazy { SupervisorJob() }

    private val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    /** Вызываем метод запроса на сервер внутри корутины */
    protected fun <T : Any> makeRequest(
        call: suspend () -> Response<T>,
        success: (response: T) -> Unit,
        errorHandler: RequestErrorHandler
    ) {
        CoroutineScope(coroutineContext).launch {
            safeApiCall(
                call = call,
                success = success,
                errorHandler = errorHandler
            )
        }
    }

    /** Делаем непосредственно запрос на сервер */
    private suspend fun <T : Any> safeApiCall(
        call: suspend () -> Response<T>,
        success: (response: T) -> Unit,
        errorHandler: RequestErrorHandler
    ) {
        //Check for network connection
        if (!NetworkUtils.isNetworkAvailable(
                App.appContext()
            )
        ) {
            withContext(Dispatchers.Main) {
                errorHandler.networkUnavailableError()
            }

            return
        }

        if (checkTokenExpired()) {
            updateTokensAndRequest(call, success, errorHandler)
        }

        //Making request
        when (val result: Result<T> = safeApiResult(call)) {
            is Result.Success -> {
                withContext(Dispatchers.Main) {
                    if (result.data == null) {
                        errorHandler.requestSuccessButResponseIsNull()
                    } else {
                        success.invoke(result.data)
                    }
                }
            }

            is Result.NetworkException -> {
                withContext(Dispatchers.Main) {
                    if (result.apiError?.exception is SocketTimeoutException || result.apiError?.exception is ConnectException) {
                        errorHandler.timeoutException()
                    } else if (result.apiError?.statusCode == 403 && !AuthUtils.isUserDefault() && AuthUtils.isUserAuthorized()) {
                        updateTokensAndRequest(call, success, errorHandler)
                    } else {
                        errorHandler.requestFailedError(result.apiError)
                    }
                }
            }
        }
    }

    /**
     * Make request.
     *
     * @param call suspend request function.
     */
    private suspend fun <T : Any> safeApiResult(call: suspend () -> Response<T>): Result<T> {
        val response = try {
            call.invoke()
        } catch (e: Exception) {
            return Result.NetworkException(ApiError(exception = e))
        }

        if (response.isSuccessful) {
            return Result.Success(response.body())
        }

        return Result.NetworkException(
            response.errorBody()?.let {
                ApiError(statusCode = response.code(), message = getErrorMessageFromErrorBody(it))
            })
    }

    /** Делаем попытку обновить токены и передаем сюда оригинальный запрос с колбэками.
    Если обновление токенов было успешно - повторяем непрошедший запрос
    Если нет - кидаем ошибки в изначальный errorHandler */
    private suspend fun <T : Any> updateTokensAndRequest(
        call: suspend () -> Response<T>,
        success: (response: T) -> Unit,
        errorHandler: RequestErrorHandler
    ) {
        when (val result: Result<UpdatedTokensModel> =
            safeApiResult(call = { RemoteDataSource.updateToken() })) {
            is Result.Success -> {
                with(AuthUtils) {
                    result.data?.let {
                        saveAuthToken(
                            it.accessToken
                        )
                        saveRefreshToken(
                            it.refreshToken
                        )
                        saveExpiredIn(
                            it.expiredIn
                        )
                    }
                }

                makeRequest(call, success, errorHandler)
            }

            is Result.NetworkException -> {
                withContext(Dispatchers.Main) {
                    if (result.apiError?.exception is SocketTimeoutException || result.apiError?.exception is ConnectException) {
                        errorHandler.timeoutException()
                    } else {
                        errorHandler.requestFailedError(result.apiError)
                    }
                }
            }
        }
    }

    /** Проверяем когда истекает access token */
    private fun checkTokenExpired() =
        if (!AuthUtils.isUserDefault() && AuthUtils.isUserAuthorized()) {
            (System.currentTimeMillis() / 1000) > AuthUtils.getExpiredIn()
        } else {
            false
        }
}
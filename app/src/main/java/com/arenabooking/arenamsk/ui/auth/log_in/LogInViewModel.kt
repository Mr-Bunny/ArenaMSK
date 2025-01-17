package com.arenabooking.arenamsk.ui.auth.log_in

import android.util.Patterns
import com.arenabooking.arenamsk.datasources.LocalDataSource
import com.arenabooking.arenamsk.network.models.ApiError
import com.arenabooking.arenamsk.network.models.RequestErrorHandler
import com.arenabooking.arenamsk.network.models.auth.LogInUserModel
import com.arenabooking.arenamsk.network.models.auth.UpdatedTokensModel
import com.arenabooking.arenamsk.network.utils.AuthUtils
import com.arenabooking.arenamsk.repositories.AuthRepository
import com.arenabooking.arenamsk.ui.base.BaseAuthViewModel
import com.arenabooking.arenamsk.utils.Constants.BAD_CREDENTIALS
import com.arenabooking.arenamsk.utils.Constants.PASSWORD_LENGTH
import com.arenabooking.arenamsk.utils.EnumUtils.LogInStatus
import com.arenabooking.arenamsk.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LogInViewModel : BaseAuthViewModel() {

    /** Статус авторизации */
    private val logInStatus = SingleLiveEvent<LogInStatus>()

    private val repository = AuthRepository.getInstance()

    /** Обработчик ошибок запроса */
    private val errorHandler = object : RequestErrorHandler {
        override suspend fun networkUnavailableError() {
            logInStatus.value = LogInStatus.NETWORK_OFFLINE
        }

        override suspend fun requestFailedError(error: ApiError?) {
            logInStatus.value = if (error?.message == BAD_CREDENTIALS) {
                LogInStatus.BAD_CREDENTIALS
            } else {
                LogInStatus.LOG_IN_FAIL
            }
        }

        override suspend fun requestSuccessButResponseIsNull() {
            logInStatus.value = LogInStatus.LOG_IN_FAIL
        }

        override suspend fun timeoutException() {
            logInStatus.value = LogInStatus.LOG_IN_FAIL
        }
    }

    fun getLogInStatus() = logInStatus

    /** Пропускаем авторизацию только после того как пользователь подтвердил что принимает политику приложения*/
    override fun skipAuth() {
        //Открываем приложение
        AuthUtils.setPoliticAccepted(true)
        logInStatus.value = LogInStatus.LOG_IN_SUCCESS

        super.skipAuth()
    }

    /** Авторизация */
    fun startAuth(email: String, password: String) {
        if (checkForError(email, password)) return

        //true если это телефон, false если email
        if (phoneIsCorrect(email)) {
            repository.startLogIn(
                LogInUserModel(number = email, password = password),
                success = ::logInSuccess,
                errorHandler = errorHandler
            )
        } else {
            repository.startLogIn(
                LogInUserModel(email = email, password = password),
                success = ::logInSuccess,
                errorHandler = errorHandler
            )
        }
    }

    /** Проверяем введенные данные на корректность */
    private fun checkForError(email: String, password: String): Boolean {
        when {
            email.isEmpty() -> {
                logInStatus.value = LogInStatus.EMAIL_EMPTY
                return true
            }

            !Patterns.EMAIL_ADDRESS.matcher(email).matches() && !phoneIsCorrect(email) -> {
                logInStatus.value = LogInStatus.EMAIL_INCORRECT
                return true
            }

            password.isEmpty() -> {
                logInStatus.value = LogInStatus.PASSWORD_EMPTY
                return true
            }

            password.length < PASSWORD_LENGTH -> {
                logInStatus.value = LogInStatus.PASSWORD_MIN_LENGTH_ERROR
                return true
            }

            else -> {
                return false
            }
        }
    }

    /** Проверяем корректностьномера телефона
     * Он должен состоять только из цифр и иметь 11 символов
     * Пример корректного ноемра 70000000000 */
    private fun phoneIsCorrect(phone: String): Boolean {
        return phone.length == 11 && (phone.toIntOrNull() == null)
    }

    /** Авторизация успешна, мы получили токены */
    private fun logInSuccess(response: UpdatedTokensModel) {
        with(AuthUtils) {
            saveAuthToken(response.accessToken)
            saveRefreshToken(response.refreshToken)
            saveExpiredIn(response.expiredIn)
        }

        //Подгружаем информацию о пользователе
        repository.getAccountInfo(
            success = {
                launch(Dispatchers.IO) {
                    //Сохраняем в БД данные пользователя
                    LocalDataSource.saveUserData(it)

                    //Сохраняем токены и ставим флаг, что авторизированы
                    with(AuthUtils) {
                        setUserIsAuthorized(true)
                        setUserIsDefault(false)
                    }

                    //Открываем приложение
                    withContext(Dispatchers.Main) {
                        logInStatus.value = LogInStatus.LOG_IN_SUCCESS
                    }
                }
            },
            errorHandler = errorHandler
        )
    }
}
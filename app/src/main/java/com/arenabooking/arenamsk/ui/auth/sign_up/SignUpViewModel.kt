package com.arenabooking.arenamsk.ui.auth.sign_up

import android.graphics.Bitmap
import android.util.Patterns
import com.arenabooking.arenamsk.datasources.LocalDataSource
import com.arenabooking.arenamsk.network.models.ApiError
import com.arenabooking.arenamsk.network.models.RequestErrorHandler
import com.arenabooking.arenamsk.network.models.auth.LogInUserModel
import com.arenabooking.arenamsk.network.models.auth.SignUpUserModel
import com.arenabooking.arenamsk.network.models.auth.UpdatedTokensModel
import com.arenabooking.arenamsk.network.utils.AuthUtils
import com.arenabooking.arenamsk.network.utils.AuthUtils.emptyErrorHandler
import com.arenabooking.arenamsk.repositories.AuthRepository
import com.arenabooking.arenamsk.room.tables.User
import com.arenabooking.arenamsk.ui.base.BaseAuthViewModel
import com.arenabooking.arenamsk.utils.Constants.EMAIL_ALREADY_EXISTS
import com.arenabooking.arenamsk.utils.Constants.PASSWORD_LENGTH
import com.arenabooking.arenamsk.utils.EnumUtils.SignUpStatus
import com.arenabooking.arenamsk.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpViewModel : BaseAuthViewModel() {

    /** Статус регистрации */
    private val signUpStatus = SingleLiveEvent<SignUpStatus>()

    private val repository = AuthRepository.getInstance()

    private lateinit var user: SignUpUserModel

    private var image: Bitmap? = null

    /** Обработчик ошибок запроса */
    private val errorHandler = object : RequestErrorHandler {
        override suspend fun networkUnavailableError() {
            signUpStatus.value = SignUpStatus.NETWORK_OFFLINE
        }

        override suspend fun requestFailedError(error: ApiError?) {
            signUpStatus.value = if (error?.message == EMAIL_ALREADY_EXISTS) {
                SignUpStatus.EMAIL_EXIST
            } else {
                SignUpStatus.SIGN_UP_FAIL
            }
        }

        override suspend fun requestSuccessButResponseIsNull() {
            signUpStatus.value = SignUpStatus.SIGN_UP_FAIL
        }

        override suspend fun timeoutException() {
            signUpStatus.value = SignUpStatus.SIGN_UP_FAIL
        }
    }

    fun getSignUpStatus() = signUpStatus

    /** Регистрация */
    fun startSignUp(name: String, email: String, password: String, image: Bitmap?) {
        if (checkForError(name, email, password)) return

        this.image = image

        //true если это телефон, false если email
        user = if (phoneIsCorrect(email)) {
            SignUpUserModel(firstName = name, email = null, number = email, password = password)
        } else {
            SignUpUserModel(firstName = name, email = email, password = password)
        }.also {
            repository.startSignUp(
                it,
                success = ::signUpSuccess,
                errorHandler = errorHandler
            )
        }
    }

    /** Проверяем на ошибки введеные пользователем значения */
    private fun checkForError(name: String, email: String, password: String): Boolean {
        when {
            name.isEmpty() -> {
                signUpStatus.value = SignUpStatus.USERNAME_EMPTY
                return true
            }

            email.isEmpty() -> {
                signUpStatus.value = SignUpStatus.EMAIL_EMPTY
                return true
            }

            !Patterns.EMAIL_ADDRESS.matcher(email).matches() && !phoneIsCorrect(email) -> {
                signUpStatus.value = SignUpStatus.EMAIL_INCORRECT
                return true
            }

            password.isEmpty() -> {
                signUpStatus.value = SignUpStatus.PASSWORD_EMPTY
                return true
            }

            password.length < PASSWORD_LENGTH -> {
                signUpStatus.value = SignUpStatus.PASSWORD_MIN_LENGTH_ERROR
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

    /** Регистрация успешна, мы получили userId */
    private fun signUpSuccess(response: User) {
        val logInUserModel = LogInUserModel(
            email = user.email ?: "",
            number = user.number,
            password = user.password
        )

        //Авторизируемся
        repository.startLogIn(
            logInUserModel,
            success = {
                launch(Dispatchers.IO) {
                    //Сохраняем в БД данные пользователя
                    val user = User(
                        userId = response.userId,
                        firstName = user.firstName,
                        email = user.email ?: "",
                        number = user.number
                    ).also {
                        LocalDataSource.saveUserData(it)
                    }

                    //Сохраняем токены и ставим флаг, что авторизированы
                    saveAuthTokens(it)

                    //Загружаем аватарку на сервер
                    uploadAvatar(user)

                    //Открываем приложение
                    withContext(Dispatchers.Main) {
                        signUpStatus.value = SignUpStatus.SIGN_UP_SUCCESS
                    }
                }
            }, errorHandler = errorHandler
        )
    }

    /** Сохраняем полученные токены в SharedPreferences */
    private fun saveAuthTokens(tokens: UpdatedTokensModel) {
        with(AuthUtils) {
            saveAuthToken(tokens.accessToken)
            saveRefreshToken(tokens.refreshToken)
            saveExpiredIn(tokens.expiredIn)
            setUserIsAuthorized(true)
            setUserIsDefault(false)
        }
    }


    /** Запрос на загрузку аватарки пользователя */
    private fun uploadAvatar(user: User) {
        //Выполняем запрос только, если пользователь выбрал картинку
        image?.let {
            repository.uploadAvatar(
                image = it,
                success = { imageLinkModel ->
                    //Сохраняем ссылку на аватарку в БД
                    user.imageUrl = imageLinkModel.imageUrl
                    launch(Dispatchers.IO) {
                        LocalDataSource.updateUserData(user)
                    }
                },
                errorHandler = emptyErrorHandler
            )
        }
    }
}
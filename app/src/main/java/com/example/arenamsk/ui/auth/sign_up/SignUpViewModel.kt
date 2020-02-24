package com.example.arenamsk.ui.auth.sign_up

import android.util.Patterns
import com.example.arenamsk.datasources.LocalDataSource
import com.example.arenamsk.network.models.ApiError
import com.example.arenamsk.network.models.RequestErrorHandler
import com.example.arenamsk.network.models.auth.LogInUserModel
import com.example.arenamsk.network.models.auth.SignUpUserModel
import com.example.arenamsk.network.utils.AuthUtils
import com.example.arenamsk.network.utils.AuthUtils.emptyErrorHandler
import com.example.arenamsk.repositories.AuthRepository
import com.example.arenamsk.room.tables.User
import com.example.arenamsk.utils.Constants.PASSWORD_LENGTH
import com.example.arenamsk.ui.base.BaseViewModel
import com.example.arenamsk.utils.EnumUtils.SignUpStatus
import com.example.arenamsk.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpViewModel : BaseViewModel() {

    private val signUpStatus = SingleLiveEvent<SignUpStatus>()

    private val repository = AuthRepository.getInstance()

    private lateinit var user: SignUpUserModel

    private var image: ByteArray? = null

    fun getSignUpStatus() = signUpStatus

    fun startSignUp(name: String, email: String, password: String, image: ByteArray?) {
        if (checkForError(name, email, password)) return

        this.image = image

        //true если это телефон, false если email
        user = if (phoneIsCorrect(email)) {
            SignUpUserModel(firstName = name, number = email, password = password)
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

    private fun phoneIsCorrect(phone: String): Boolean {
        return phone.length == 11 && (phone.toIntOrNull() == null)
    }

    private val errorHandler = object : RequestErrorHandler {
        override suspend fun networkUnavailableError() {
            signUpStatus.value = SignUpStatus.NETWORK_OFFLINE
        }

        override suspend fun requestFailedError(error: ApiError?) {
            signUpStatus.value = SignUpStatus.SIGN_UP_FAIL
        }

        override suspend fun requestSuccessButResponseIsNull() {
            signUpStatus.value = SignUpStatus.SIGN_UP_FAIL
        }

        override suspend fun timeoutException() {
            signUpStatus.value = SignUpStatus.SIGN_UP_FAIL
        }
    }

    //Регистрация успешна, мы получили userId
    private fun signUpSuccess(response: User) {
        val logInUserModel = LogInUserModel(
            email = user.email,
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
                        email = user.email,
                        number = user.number
                    ).also {
                        LocalDataSource.saveUserData(it)
                    }

                    //Сохраняем токены и ставим флаг, что авторизированы
                    with(AuthUtils) {
                        saveAuthToken(it.accessToken)
                        saveRefreshToken(it.refreshToken)
                        setUserIsAuthorized(true)
                    }

                    //Загружаем аватарку на сервер
                    image?.let {
                        repository.uploadAvatar(
                            image = it,
                            success = {
                                //Сохраняем ссылку на аватарку в БД
                                user.imageUrl = it
                                launch(Dispatchers.IO) {
                                    LocalDataSource.updateUserData(user)
                                }
                            },
                            errorHandler = emptyErrorHandler
                        )
                    }

                    //Открываем приложение
                    withContext(Dispatchers.Main) {
                        signUpStatus.value = SignUpStatus.SIGN_UP_SUCCESS
                    }
                }
            }, errorHandler = errorHandler
        )
    }
}
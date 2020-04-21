package com.example.arenamsk.repositories

import android.graphics.Bitmap
import com.example.arenamsk.datasources.RemoteDataSource
import com.example.arenamsk.network.models.RequestErrorHandler
import com.example.arenamsk.network.models.auth.*
import com.example.arenamsk.room.tables.User
import com.example.arenamsk.utils.ImageUtils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

/** Реопзиторий связанный с авторизацией */
class AuthRepository private constructor() : BaseRepository() {

    companion object {
        @Volatile
        private var repositoryInstance: AuthRepository? = null

        fun getInstance() = repositoryInstance ?: synchronized(this) {
            repositoryInstance ?: AuthRepository()
        }
    }

    /** Регистрация */
    fun startSignUp(
        model: SignUpUserModel,
        success: (response: User) -> Unit,
        errorHandler: RequestErrorHandler
    ) = makeRequest(
        call = { RemoteDataSource.startSignUp(model) },
        success = success,
        errorHandler = errorHandler
    )

    /** Загрузка аватарки */
    fun uploadAvatar(
        image: Bitmap,
        success: (response: UserImageUrl) -> Unit,
        errorHandler: RequestErrorHandler
    ) = makeRequest(
        call = {
            val file = ImageUtils.createFileFromBitmap(image)

            val filePart = MultipartBody.Part.createFormData(
                "file",
                file.name,
                RequestBody.create(MediaType.parse("image/*"), file)
            )

            RemoteDataSource.uploadAvatar(filePart)
        },
        success = success,
        errorHandler = errorHandler
    )

    /** Авторизация */
    fun startLogIn(
        model: LogInUserModel,
        success: (response: UpdatedTokensModel) -> Unit,
        errorHandler: RequestErrorHandler
    ) = makeRequest(
        call = { RemoteDataSource.startLogIn(model) },
        success = success,
        errorHandler = errorHandler
    )

    /** Загрузка информации о пользователе */
    fun getAccountInfo(
        success: (response: User) -> Unit,
        errorHandler: RequestErrorHandler
    ) = makeRequest(
        call = { RemoteDataSource.getAccountInfo() },
        success = success,
        errorHandler = errorHandler
    )

    /** Смена пароля */
    fun changePassword(
        currentPassword: String,
        newPassword: String,
        success: (Unit) -> Unit,
        errorHandler: RequestErrorHandler
    ) = makeRequest(
        call = { RemoteDataSource.changePassword(PasswordChangeDto(currentPassword, newPassword)) },
        success = success,
        errorHandler = errorHandler
    )
}
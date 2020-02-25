package com.example.arenamsk.repositories

import android.graphics.Bitmap
import com.example.arenamsk.datasources.RemoteDataSource
import com.example.arenamsk.network.models.RequestErrorHandler
import com.example.arenamsk.network.models.auth.LogInUserModel
import com.example.arenamsk.network.models.auth.SignUpUserModel
import com.example.arenamsk.network.models.auth.UpdatedTokensModel
import com.example.arenamsk.network.models.auth.UserImageUrl
import com.example.arenamsk.network.utils.BaseRepository
import com.example.arenamsk.room.tables.User
import com.example.arenamsk.utils.ImageUtils
import com.google.gson.JsonObject
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class AuthRepository private constructor() : BaseRepository() {

    companion object {
        @Volatile
        private var repositoryInstance: AuthRepository? = null

        fun getInstance() = repositoryInstance ?: synchronized(this) {
            repositoryInstance ?: AuthRepository()
        }
    }

    fun startSignUp(
        model: SignUpUserModel,
        success: (response: User) -> Unit,
        errorHandler: RequestErrorHandler
    ) = makeRequest(
        call = { RemoteDataSource.startSignUp(model) },
        success = success,
        errorHandler = errorHandler
    )

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

    fun startLogIn(
        model: LogInUserModel,
        success: (response: UpdatedTokensModel) -> Unit,
        errorHandler: RequestErrorHandler
    ) = makeRequest(
        call = { RemoteDataSource.startLogIn(model) },
        success = success,
        errorHandler = errorHandler
    )

    fun getAccountInfo(
        success: (response: User) -> Unit,
        errorHandler: RequestErrorHandler
    ) = makeRequest(
        call = { RemoteDataSource.getAccountInfo() },
        success = success,
        errorHandler = errorHandler
    )

    fun test(
        success: (response: JsonObject) -> Unit,
        errorHandler: RequestErrorHandler
    ) = makeRequest(
        call = { RemoteDataSource.testClosed() },
        success = success,
        errorHandler = errorHandler
    )

}
package com.example.arenamsk.repositories

import com.example.arenamsk.datasources.RemoteDataSource
import com.example.arenamsk.network.models.RequestErrorHandler
import com.example.arenamsk.network.models.auth.LogInUserModel
import com.example.arenamsk.network.models.auth.SignUpUserModel
import com.example.arenamsk.network.models.auth.UpdatedTokensModel
import com.example.arenamsk.network.utils.BaseRepository
import com.example.arenamsk.room.tables.User
import com.google.gson.JsonObject
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

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
        image: ByteArray,
        success: (response: String) -> Unit,
        errorHandler: RequestErrorHandler
    ) = makeRequest(
        call = {
            val imagePart = RequestBody.create(MediaType.parse("image/jpeg"), image)
            val imageMultipart = MultipartBody.Part.createFormData("image", System.currentTimeMillis().toString(), imagePart)
            RemoteDataSource.uploadAvatar(imageMultipart)
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
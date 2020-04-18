package com.example.arenamsk.ui.settings

import com.example.arenamsk.network.models.RequestErrorHandler
import com.example.arenamsk.repositories.AuthRepository
import com.example.arenamsk.repositories.PlaceRepository
import com.example.arenamsk.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel : BaseViewModel() {

    private val repository = AuthRepository.getInstance()

    /** Запрос смены пароля */
    fun changePassword(
        currentPassword: String,
        newPassword: String,
        success: (Unit) -> Unit,
        errorHandler: RequestErrorHandler
    ) {
        launch(Dispatchers.IO) {
            repository.changePassword(currentPassword, newPassword, success, errorHandler)
        }
    }
}
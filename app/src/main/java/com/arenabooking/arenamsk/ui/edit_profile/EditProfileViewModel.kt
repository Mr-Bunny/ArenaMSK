package com.arenabooking.arenamsk.ui.edit_profile

import com.arenabooking.arenamsk.network.models.RequestErrorHandler
import com.arenabooking.arenamsk.repositories.AuthRepository
import com.arenabooking.arenamsk.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditProfileViewModel : BaseViewModel() {

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
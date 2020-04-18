package com.example.arenamsk.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.arenamsk.R
import com.example.arenamsk.network.models.ApiError
import com.example.arenamsk.network.models.RequestErrorHandler
import com.example.arenamsk.room.tables.Subway
import com.example.arenamsk.ui.MainActivity
import com.example.arenamsk.ui.base.BaseFragment
import com.example.arenamsk.ui.places.PlacesViewModel
import com.example.arenamsk.utils.SharedPreferenceManager
import com.example.arenamsk.utils.SharedPreferenceManager.KEY.NOTIFICATION_IS_ENABLED
import com.example.arenamsk.utils.SharedPreferenceManager.KEY.NOTIFICATION_TIME
import kotlinx.android.synthetic.main.fragment_settings.*
import org.angmarch.views.OnSpinnerItemSelectedListener

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    companion object {
        private const val HOUR_1 = 1
        private const val HOUR_6 = 6
        private const val HOUR_12 = 12
        private const val HOUR_24 = 24
    }


    private val settingsViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(SettingsViewModel::class.java)
    }

    private val dispatcher by lazy { requireActivity().onBackPressedDispatcher }

    private val errorHandler = object : RequestErrorHandler {
        override suspend fun networkUnavailableError() {
            showToast("Нет соединения с интернетом")
        }

        override suspend fun requestFailedError(error: ApiError?) {
            showToast("Не удалось поменять пароль, проверьте введенные данные")
        }

        override suspend fun timeoutException() {
            showToast("Не удалось поменять пароль, проверьте введенные данные")
        }

        override suspend fun requestSuccessButResponseIsNull() {
            showToast("Не удалось поменять пароль, проверьте введенные данные")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dispatcher.addCallback(this) {
            findNavController().popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        current_password_edit_text.setHintText("Текущий пароль")
        new_password_edit_text.setHintText("Новый пароль")

        with(spinner_notifications) {
            attachDataSource(resources.getStringArray(R.array.notifications_titles).toList())
            onSpinnerItemSelectedListener =
                OnSpinnerItemSelectedListener { _, _, position, _ ->
                    when (position) {
                        0 -> {
                            setupNotifications()
                        }

                        1 -> {
                            setupNotifications(HOUR_1)
                        }

                        2 -> {
                            setupNotifications(HOUR_6)
                        }

                        3 -> {
                            setupNotifications(HOUR_12)
                        }

                        4 -> {
                            setupNotifications(HOUR_24)
                        }
                    }
                }
        }

        initSettings()

        settings_geo_btn.setOnClickListener { requireActivity().startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }

        change_password_btn.setOnClickListener {
            val currentPassword = current_password_edit_text.getEditText().text.toString()
            val newPassword = new_password_edit_text.getEditText().text.toString()

            if (currentPassword.isEmpty()) {
                showToast("Введите текущий пароль")
            } else if (newPassword.isEmpty()) {
                showToast("Введите новый пароль")
            } else if (currentPassword.length < 6 || newPassword.length < 6) {
                showToast("Минимальная длина пароля 6 символов")
            } else {
                settingsViewModel.changePassword(
                    currentPassword = current_password_edit_text.getEditText().text.toString(),
                    newPassword = new_password_edit_text.getEditText().text.toString(),
                    success = {
                        current_password_edit_text.getEditText().setText("")
                        new_password_edit_text.getEditText().setText("")
                        showToast("Пароль изменен!")
                    },
                    errorHandler = errorHandler
                )
            }
        }
    }

    /** Выставляем сохраненные настройки уведомлений */
    private fun initSettings() {
        if (SharedPreferenceManager.getInstance().getBooleanValue(
                NOTIFICATION_IS_ENABLED,
                false
            ) == true
        ) {
            when (SharedPreferenceManager.getInstance().getIntValue(NOTIFICATION_TIME, 0)) {
                0 -> {
                    spinner_notifications.selectedIndex = 0
                }

                HOUR_1 -> {
                    spinner_notifications.selectedIndex = 1
                }

                HOUR_6 -> {
                    spinner_notifications.selectedIndex = 2
                }

                HOUR_12 -> {
                    spinner_notifications.selectedIndex = 3
                }

                HOUR_24 -> {
                    spinner_notifications.selectedIndex = 4
                }
            }
        } else {
            spinner_notifications.selectedIndex = 0
        }
    }

    /** Включаем или отключаем уведолмения за определенное кол-во часов до брони */
    private fun setupNotifications(hours: Int = 0) {
        //Сохраняем флаг включены уведомления или нет
        SharedPreferenceManager.getInstance().saveValue(NOTIFICATION_IS_ENABLED, hours != 0)
        //Сохраняем кол-во часов для уведолмений
        SharedPreferenceManager.getInstance().saveValue(NOTIFICATION_TIME, hours)
    }
}

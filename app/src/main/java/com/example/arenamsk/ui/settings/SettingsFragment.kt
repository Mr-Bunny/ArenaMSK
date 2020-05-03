package com.example.arenamsk.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.example.arenamsk.FCMService
import com.example.arenamsk.R
import com.example.arenamsk.network.utils.AuthUtils
import com.example.arenamsk.ui.base.BaseFragment
import com.example.arenamsk.utils.SharedPreferenceManager
import com.example.arenamsk.utils.SharedPreferenceManager.KEY.NOTIFICATION_IS_ENABLED
import com.example.arenamsk.utils.SharedPreferenceManager.KEY.NOTIFICATION_TIME
import kotlinx.android.synthetic.main.fragment_settings.*
import org.angmarch.views.OnSpinnerItemSelectedListener

/** Экран настроек приложения
 * Здесь можно поменять уведомления и перейти в настройки передачи гео-данных */
class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    companion object {
        private const val HOUR_1 = 1
        private const val HOUR_6 = 6
        private const val HOUR_12 = 12
        private const val HOUR_24 = 24
    }

    private val dispatcher by lazy { requireActivity().onBackPressedDispatcher }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dispatcher.addCallback(this) {
            findNavController().popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (AuthUtils.isUserDefault()) {
            notification_container.setOnClickListener {
                showUnauthorizedDialog()
            }

            spinner_notifications.isClickable = false
        }

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

        //Отправляем firebase токен, если он не был отправлен
        if (SharedPreferenceManager.getInstance().getBooleanValue(
                SharedPreferenceManager.KEY.FCM_TOKEN_UPDATED, false) == false) {
            with(FCMService) {
                sendToken(getCurrentFCMToken())
            }
        }
    }

    /** Если пользователь не авторизирован - показываем диалоговое окно с кнопкой для перехода к регистрации */
    private fun showUnauthorizedDialog() {
        AlertDialog.Builder(requireContext(), R.style.FavouriteAlertDialog)
            .setTitle("Вы не авторизированы")
            .setMessage("Для получения уведомлений необходимо зарегистрироваться")
            .setPositiveButton("Зарегистрироваться") { _, _ ->
                exitFromProfile()
            }
            .setNegativeButton("Закрыть") { dialog, _ ->
                dialog.cancel()
            }
            .setCancelable(false)
            .show()
    }
}

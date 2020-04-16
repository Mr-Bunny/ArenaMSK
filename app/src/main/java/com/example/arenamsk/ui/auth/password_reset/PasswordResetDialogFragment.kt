package com.example.arenamsk.ui.auth.password_reset

import android.app.Dialog
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.arenamsk.R
import com.example.arenamsk.network.models.ApiError
import com.example.arenamsk.network.models.RequestErrorHandler
import com.example.arenamsk.repositories.PlaceRepository
import kotlinx.android.synthetic.main.fragment_password_reset.*

class PasswordResetDialogFragment private constructor() : DialogFragment() {

    companion object {
        const val RESET_TAG = "reset_tag"

        fun getInstance(): PasswordResetDialogFragment {
            return PasswordResetDialogFragment()
        }
    }

    private val repository = PlaceRepository.getInstance()

    private var errorToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(activity!!, theme) {
            override fun onBackPressed() {
                dismiss()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_password_reset,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        send_btn.setOnClickListener {
            checkInputData()
        }
    }

    /** Проверяем введенный email или телефон, если есть в базе, высылаем на него новый пароль */
    private fun checkInputData() {
        val email = email_edit_text.getEditText().text.toString()

        when {
            email.isEmpty() -> {
                showToast("Введите email или телефон")
            }

            !Patterns.EMAIL_ADDRESS.matcher(email).matches() && !phoneIsCorrect(email) -> {
                showToast("Проверьте введенные данные")
            }

            else -> {
                repository.sendEmailToResetPassword(
                    email = email,
                    success = {
                        showToast("Мы отправили вам новый пароль!")
                        dismiss()
                    },
                    errorHandler = object : RequestErrorHandler {
                        override suspend fun networkUnavailableError() {
                            showToast("Нет соединения с интернетом")
                        }

                        override suspend fun requestFailedError(error: ApiError?) {
                            showToast("Не удалось выслать новый пароль. Проверьте введенные данные")
                        }

                        override suspend fun requestSuccessButResponseIsNull() {
                        }

                        override suspend fun timeoutException() {
                            showToast("Не удалось выслать новый пароль. Проверьте введенные данные")
                        }
                    }
                )
            }
        }
    }

    /** Проверяем корректностьномера телефона
     * Он должен состоять только из цифр и иметь 11 символов
     * Пример корректного ноемра 70000000000 */
    private fun phoneIsCorrect(phone: String): Boolean {
        return phone.length == 11 && (phone.toIntOrNull() == null)
    }

    /** Показываем toast с текстом ошибки */
    private fun showToast(msg: String) {
        errorToast?.cancel()
        errorToast = Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG)
        errorToast?.show()
    }

}
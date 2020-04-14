package com.example.arenamsk.ui.feedback

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import com.example.arenamsk.R
import com.example.arenamsk.models.FeedbackModel
import com.example.arenamsk.network.models.ApiError
import com.example.arenamsk.network.models.AppFeedbackModel
import com.example.arenamsk.network.models.RequestErrorHandler
import com.example.arenamsk.repositories.PlaceRepository
import com.example.arenamsk.utils.TimeUtils
import com.example.arenamsk.utils.enable
import kotlinx.android.synthetic.main.fragment_feedback.*

class FeedbackDialogFragment private constructor() : DialogFragment(), LifecycleOwner {

    companion object {
        const val FEEDBACK_TAG = "feedback_tag"
        private const val ARGUMENT = "arg"
        private const val PLACE_ID_ARG = "place_id_arg"

        //isPlaceFeedback - флаг, true если нужно послать отзыв о площадке, false - послать отзыв о приложении
        fun getInstance(
            isPlaceFeedback: Boolean = false,
            placeId: String = ""
        ): FeedbackDialogFragment {
            return FeedbackDialogFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARGUMENT, isPlaceFeedback)
                    putString(PLACE_ID_ARG, placeId)
                }
            }
        }
    }

    private var errorToast: Toast? = null

    private var isRecommended = false

    private val isPlaceFeedback by lazy { arguments?.getBoolean(ARGUMENT, false) ?: false }

    private val placeId by lazy { arguments?.getString(PLACE_ID_ARG, "") ?: "" }

    private val repository = PlaceRepository.getInstance()

    private val errorHandler = object : RequestErrorHandler {
        override suspend fun networkUnavailableError() {
            showToast("Нет соединения с интернетом")
            feedback_send_btn.isClickable = true
        }

        override suspend fun requestFailedError(error: ApiError?) {
            showToast("Не удалось отправить отзыв")
            feedback_send_btn.isClickable = true
        }

        override suspend fun requestSuccessButResponseIsNull() {
            feedbackSent()
        }

        override suspend fun timeoutException() {
            showToast("Не удалось отправить отзыв")
            feedback_send_btn.isClickable = true
        }
    }

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
            R.layout.fragment_feedback,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isPlaceFeedback) {
            feedback_mark_title.enable()
            feedback_rating.enable()
            feedback_name_edit_text.enable()
            feedback_recommendation.enable()

            feedback_recommendation.setOnClickListener {
                isRecommended = !isRecommended
                val leftDrawable = AppCompatResources.getDrawable(
                    requireContext(),
                    if (isRecommended) R.drawable.ic_recommendation_red else R.drawable.ic_recommendation_grey
                )
                feedback_recommendation.setCompoundDrawablesWithIntrinsicBounds(
                    leftDrawable,
                    null,
                    null,
                    null
                )
            }
        }

        feedback_send_btn.setOnClickListener {
            sendFeedback(
                name = feedback_name_edit_text.text.toString().trim(),
                text = feedback_edit_text.text.toString().trim(),
                rating = feedback_rating.rating
            )
        }
    }

    /** Отправляем наш отзыв
     * @param name - Имя автора
     * @param text - Текст отзыва
     * @param rating - Выставленный рейтинг (по умолчанию 0) */
    private fun sendFeedback(name: String, text: String, rating: Float) {
        if (isPlaceFeedback && name.isEmpty()) {
            showToast("Имя не может быть пустым")
        } else if (text.isEmpty()) {
            showToast("Отзыв не может быть пустым")
        } else {
            //Делаем кнопку отправки недоступной
            feedback_send_btn.isClickable = false

            with(repository) {
                if (isPlaceFeedback) {
                    sendPlaceFeedback(
                        placeId = placeId,
                        feedback = FeedbackModel(
                            authorName = name,
                            date = System.currentTimeMillis().toString(),
                            isRecommendation = isRecommended,
                            rating = rating,
                            feedbackText = text
                        ),
                        success = { feedbackSent() },
                        errorHandler = errorHandler
                    )
                } else {
                    sendAppFeedback(AppFeedbackModel(text),
                        success = { feedbackSent() },
                        errorHandler = errorHandler)
                }
            }

        }
    }

    /** Показываем toast с текстом ошибки */
    private fun showToast(msg: String) {
        errorToast?.cancel()
        errorToast = Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG)
        errorToast?.show()
    }

    /** Сбрасываем все измененные значения */
    private fun feedbackSent() {
        showToast("Отзыв отправлен!")
        feedback_name_edit_text.setText("")
        feedback_edit_text.setText("")
        feedback_send_btn.isClickable = true
        feedback_rating.rating = 0f
        if (isRecommended) feedback_recommendation.callOnClick()
    }
}
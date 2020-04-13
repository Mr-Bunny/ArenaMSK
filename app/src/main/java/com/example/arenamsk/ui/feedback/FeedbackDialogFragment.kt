package com.example.arenamsk.ui.feedback

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import com.example.arenamsk.R
import com.example.arenamsk.utils.enable
import kotlinx.android.synthetic.main.fragment_feedback.*

class FeedbackDialogFragment private constructor() : DialogFragment(), LifecycleOwner {

    companion object {
        const val FEEDBACK_TAG = "feedback_tag"
        private const val ARGUMENT = "arg"

        //isPlaceFeedback - флаг, true если нужно послать отзыв о площадке, false - послать отзыв о приложении
        fun getInstance(isPlaceFeedback: Boolean): FeedbackDialogFragment {
            return FeedbackDialogFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARGUMENT, isPlaceFeedback)
                }
            }
        }
    }

    private var isRecommended = false

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

        val isPlaceFeedback = arguments?.getBoolean(ARGUMENT, false) ?: false

        if (isPlaceFeedback) {
            feedback_mark_title.enable()
            feedback_rating.enable()
            feedback_name_edit_text.enable()
            feedback_recommendation.enable()

            feedback_recommendation.setOnClickListener {
                isRecommended = !isRecommended
                val leftDrawable = AppCompatResources.getDrawable(requireContext(), if (isRecommended) R.drawable.ic_recommendation_red else R.drawable.ic_recommendation_grey)
                feedback_recommendation.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null)
            }
        }
    }
}
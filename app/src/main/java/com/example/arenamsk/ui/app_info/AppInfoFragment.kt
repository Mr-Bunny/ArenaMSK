package com.example.arenamsk.ui.app_info

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.example.arenamsk.R
import com.example.arenamsk.datasources.LocalDataSource
import com.example.arenamsk.network.utils.AuthUtils
import com.example.arenamsk.ui.base.BaseFragment
import com.example.arenamsk.ui.feedback.FeedbackDialogFragment
import com.example.arenamsk.utils.NotificationsUtils
import com.example.arenamsk.utils.disable
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_app_info.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.*

/** Экран с текстом политики конфиденциальности или условий */
class AppInfoDialogFragment private constructor() : DialogFragment(), LifecycleOwner {

    companion object {
        const val INFO_TAG = "app_info_tag"
        private const val ARGUMENT = "arg"

        fun getInstance(text: String = ""): AppInfoDialogFragment {
            return AppInfoDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARGUMENT, text)
                }
            }
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
            R.layout.fragment_app_info,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val text = arguments?.getString(ARGUMENT) ?: ""
        main_text.text = text
    }
}
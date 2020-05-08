package com.example.arenamsk.ui.app_policy

import android.app.Dialog
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import com.example.arenamsk.R
import com.example.arenamsk.ui.auth.log_in.LogInViewModel
import kotlinx.android.synthetic.main.fragment_app_info.*
import kotlinx.android.synthetic.main.fragment_app_info.acception_text
import kotlinx.android.synthetic.main.fragment_sign_up.*

/** Экран с текстом гиперссылками политики конфиденциальности или условий
 * Поялвяется если пользователь хочет пропустить авторизацию */
class AppPolicyDialogFragment private constructor() : DialogFragment(), LifecycleOwner {

    companion object {
        const val POLICY_TAG = "app_policy_tag"

        fun getInstance(): AppPolicyDialogFragment {
            return AppPolicyDialogFragment()
        }
    }

    private val logInViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(LogInViewModel::class.java)
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

        acception_text.movementMethod = LinkMovementMethod.getInstance()

        btn_accept.setOnClickListener { logInViewModel.skipAuth() }
    }
}
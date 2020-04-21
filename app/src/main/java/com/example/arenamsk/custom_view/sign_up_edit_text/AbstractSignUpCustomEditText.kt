package com.example.arenamsk.custom_view.sign_up_edit_text

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import com.example.arenamsk.R
import com.example.arenamsk.custom_view.auth_edit_text.AbstractCustomEditText
import kotlinx.android.synthetic.main.custom_email_edit_text.view.*

/** Абстрактный класс кастомного поля ввода (EditText) на экране регистрации */
abstract class AbstractSignUpCustomEditText : AbstractCustomEditText {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    /** Обновляем editText в зависимости от состояния */
    override fun updateView() {
        when (currentState) {
            Companion.State.EMPTY -> {
                getEditText().background =
                    resources.getDrawable(R.drawable.edit_text_sign_up_background)
                custom_text_layout.hint = hint
                custom_text_layout.defaultHintTextColor =
                    ColorStateList.valueOf(resources.getColor(R.color.edit_text_highlight_hint_color))
                custom_text_layout.hintTextColor =
                    ColorStateList.valueOf(resources.getColor(R.color.edit_text_highlight_hint_color))
            }

            Companion.State.FILLED -> {
                getEditText().background =
                    resources.getDrawable(R.drawable.edit_text_filled_background)
                custom_text_layout.hint = hint
                custom_text_layout.defaultHintTextColor =
                    ColorStateList.valueOf(resources.getColor(R.color.edit_text_highlight_hint_color))
                custom_text_layout.hintTextColor =
                    ColorStateList.valueOf(resources.getColor(R.color.edit_text_highlight_hint_color))
            }

            Companion.State.ERROR -> {
                getEditText().background =
                    resources.getDrawable(R.drawable.edit_text_error_background)
                custom_text_layout.hint = errorHint
                custom_text_layout.defaultHintTextColor =
                    ColorStateList.valueOf(resources.getColor(R.color.edit_text_hint_error))
            }
        }
    }
}
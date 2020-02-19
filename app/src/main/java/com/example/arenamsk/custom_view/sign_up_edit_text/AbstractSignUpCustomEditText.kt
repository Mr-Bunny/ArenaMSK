package com.example.arenamsk.custom_view.sign_up_edit_text

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.widget.LinearLayout
import com.example.arenamsk.R
import kotlinx.android.synthetic.main.custom_email_edit_text.view.*

abstract class AbstractSignUpCustomEditText : LinearLayout {

    companion object {
        enum class State {
            EMPTY,
            FILLED,
            ERROR
        }
    }

    //Сохраняем сюда текст подсказки
    private var hint: String = ""

    //Сохраняем сюда текст ошибки
    private var errorHint: String = ""

    var currentState: State =
        State.EMPTY
        set(value) {
            field = value
            updateView()
        }


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    open fun init() {
        hint = custom_text_layout.hint.toString()

        custom_edit_text.setOnFocusChangeListener { _, hasFocus ->
            currentState = if (hasFocus) {
                State.FILLED
            } else if (!hasFocus && custom_edit_text.text.toString().isEmpty()) {
                State.EMPTY
            } else {
                State.FILLED
            }
        }
    }

    open fun getText() = custom_edit_text.text.toString()

    fun setError(errorMsg: String) {
        this.errorHint = errorMsg
        currentState =
            State.ERROR
    }

    private fun updateView() {
        when (currentState) {
            State.EMPTY -> {
                custom_edit_text.background =
                    resources.getDrawable(R.drawable.edit_text_sign_up_background)
                custom_text_layout.hint = hint
                custom_text_layout.defaultHintTextColor =
                    ColorStateList.valueOf(resources.getColor(R.color.edit_text_highlight_hint_color))
                custom_text_layout.hintTextColor =
                    ColorStateList.valueOf(resources.getColor(R.color.edit_text_highlight_hint_color))
            }

            State.FILLED -> {
                custom_edit_text.background =
                    resources.getDrawable(R.drawable.edit_text_filled_background)
                custom_text_layout.hint = hint
                custom_text_layout.defaultHintTextColor =
                    ColorStateList.valueOf(resources.getColor(R.color.edit_text_highlight_hint_color))
                custom_text_layout.hintTextColor =
                    ColorStateList.valueOf(resources.getColor(R.color.edit_text_highlight_hint_color))
            }

            State.ERROR -> {
                custom_edit_text.background =
                    resources.getDrawable(R.drawable.edit_text_error_background)
                custom_text_layout.hint = errorHint
                custom_text_layout.defaultHintTextColor =
                    ColorStateList.valueOf(resources.getColor(R.color.edit_text_hint_error))
            }
        }
    }
}
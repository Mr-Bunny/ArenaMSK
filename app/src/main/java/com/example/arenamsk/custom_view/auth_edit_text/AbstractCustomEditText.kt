package com.example.arenamsk.custom_view.auth_edit_text

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.example.arenamsk.R
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.custom_email_edit_text.view.*

abstract class AbstractCustomEditText : LinearLayout {

    protected companion object {
        enum class State {
            EMPTY,
            FILLED,
            ERROR
        }
    }

    //Сохраняем сюда текст подсказки
    protected var hint: String = ""

    //Сохраняем сюда текст ошибки
    protected var errorHint: String = ""

    protected var currentState: State =
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

    abstract fun getEditText(): TextInputEditText

    open fun init() {
        hint = custom_text_layout.hint.toString()

        getEditText().setOnFocusChangeListener { _, hasFocus ->
            currentState = if (hasFocus) {
                State.FILLED
            } else if (!hasFocus && getEditText().text.toString().isEmpty()) {
                State.EMPTY
            } else {
                State.FILLED
            }
        }
    }

    open fun getText() = getEditText().text.toString()

    protected open fun updateView() {
        when (currentState) {
            State.EMPTY -> {
                getEditText().background =
                    resources.getDrawable(R.drawable.edit_text_empty_background)
                custom_text_layout.hint = hint
                custom_text_layout.defaultHintTextColor = ColorStateList.valueOf(
                    resources.getColor(
                        R.color.edit_text_hint_color
                    )
                )
                custom_text_layout.hintTextColor =
                    ColorStateList.valueOf(resources.getColor(R.color.edit_text_highlight_hint_color))
            }

            State.FILLED -> {
                getEditText().background =
                    resources.getDrawable(R.drawable.edit_text_filled_background)
                custom_text_layout.hint = hint
                custom_text_layout.defaultHintTextColor = ColorStateList.valueOf(
                    resources.getColor(
                        R.color.edit_text_highlight_hint_color
                    )
                )
                custom_text_layout.hintTextColor =
                    ColorStateList.valueOf(resources.getColor(R.color.edit_text_highlight_hint_color))
            }

            State.ERROR -> {
                getEditText().background =
                    resources.getDrawable(R.drawable.edit_text_error_background)
                custom_text_layout.hint = errorHint
                custom_text_layout.defaultHintTextColor =
                    ColorStateList.valueOf(resources.getColor(R.color.edit_text_hint_error))
            }
        }
    }

    fun setError(errorMsg: String) {
        this.errorHint = errorMsg
        currentState =
            State.ERROR
    }
}
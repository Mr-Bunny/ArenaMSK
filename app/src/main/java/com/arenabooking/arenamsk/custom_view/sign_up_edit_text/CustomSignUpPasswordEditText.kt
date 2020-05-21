package com.arenabooking.arenamsk.custom_view.sign_up_edit_text

import android.content.Context
import android.util.AttributeSet
import com.arenabooking.arenamsk.R
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.custom_sign_up_password_edit_text.view.*

/** Кастомный editText для ввода пароля на экране регистрации */
class CustomSignUpPasswordEditText:
    AbstractSignUpCustomEditText {

    constructor(context: Context): super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int): super(context, attrs, defStyle) {
        init()
    }

    override fun init() {
        inflate(context, R.layout.custom_sign_up_password_edit_text, this)

        super.init()
    }

    override fun getEditText(): TextInputEditText = custom_sign_up_password_edit_text

    fun setHintText(hint: String) {
        custom_sign_up_password_edit_text.hint = hint
    }
}
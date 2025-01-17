package com.arenabooking.arenamsk.custom_view.sign_up_edit_text

import android.content.Context
import android.util.AttributeSet
import com.arenabooking.arenamsk.R
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.custom_sign_up_email_edit_text.view.*

/** Кастомный editText для ввода email на экране регистрации */
class CustomSignUpEmailEditText:
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
        inflate(context, R.layout.custom_sign_up_email_edit_text, this)

        super.init()
    }

    override fun getText(): String {
        with(custom_sign_up_email_edit_text) {
            return if (text.toString().startsWith("+")) {
                text.toString().substring(3)
            } else {
                text.toString()
            }
        }
    }

    override fun getEditText(): TextInputEditText = custom_sign_up_email_edit_text
}
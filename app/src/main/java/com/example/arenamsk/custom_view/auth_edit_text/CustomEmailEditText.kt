package com.example.arenamsk.custom_view.auth_edit_text

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.example.arenamsk.R
import kotlinx.android.synthetic.main.custom_email_edit_text.view.*

class CustomEmailEditText: AbstractCustomEditText {

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
        inflate(context, R.layout.custom_email_edit_text, this)

        super.init()
    }

    override fun getText(): String {
        with(custom_edit_text) {
            return if (text.toString().startsWith("+")) {
                text.toString().substring(3)
            } else {
                text.toString()
            }
        }
    }
}